package com.andrei.multithead.section8;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringJoiner;

public class MatricesApplication {
    private static String INPUT_FILE = "/home/andrei/IdeaProjects/multithread/src/main/resources/out/matrices";
    private static String OUTPUT_FILE = "/home/andrei/IdeaProjects/multithread/src/main/resources/out/matrices_results.txt";

    private static final int N = 10;

    public static void main(String[] args) throws IOException {

        ThreadSafeQueue threadSafeQueue = new ThreadSafeQueue();
        File inputFile = new File(INPUT_FILE);
        File outputFile = new File(OUTPUT_FILE);

        MatrixReaderProducer matrixProducer = new MatrixReaderProducer(new FileReader(inputFile), threadSafeQueue);
        MatrixMultiplierConsumer matrixConsumer = new MatrixMultiplierConsumer(new FileWriter(outputFile), threadSafeQueue);

        matrixConsumer.start();
        matrixProducer.start();

    }

    public static class MatrixMultiplierConsumer extends Thread {
        private ThreadSafeQueue queue;
        private FileWriter fileWriter;

        public MatrixMultiplierConsumer(FileWriter fileWriter, ThreadSafeQueue queue) {
            this.queue = queue;
            this.fileWriter = fileWriter;
        }

        @Override
        public void run() {
            while (true) {
                MatricesPair matricesPair = queue.remove();
                if (matricesPair == null) {
                    System.out.println("No more matrices to read from the queue, consumer is terminated");
                    break;
                }

                float[][] result = multiplyMatrices(matricesPair.matrix1, matricesPair.matrix2);

                try {
                    saveMatricesToFile(fileWriter, result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void saveMatricesToFile(FileWriter fileWriter, float[][] matrix) throws IOException {
            for (int row = 0; row < N; row++) {
                StringJoiner joiner = new StringJoiner(", ");
                for (int column = 0; column < N; column++) {
                    joiner.add(String.format(".%2f", matrix[row][column]));
                }
                fileWriter.write(joiner.toString());
                fileWriter.write("\n");
            }
            fileWriter.write("\n");
        }

        private float[][] multiplyMatrices(float[][] matrix1, float[][] matrix2) {
            float[][] result = new float[N][N];
            for (int row = 0; row < N; row++) {
                for (int column = 0; column < N; column++) {
                    for (int i = 0; i < N; i++) {
                        result[row][column] += matrix1[row][column] * matrix2[row][column];
                    }

                }
            }
            return result;
        }


    }

    public static class MatrixReaderProducer extends Thread {
        private Scanner scanner;
        private ThreadSafeQueue queue;

        public MatrixReaderProducer(FileReader reader, ThreadSafeQueue queue) {
            this.scanner = new Scanner(reader);
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                float[][] matrix1 = readMatrix();
                float[][] matrix2 = readMatrix();

                if (matrix1 == null || matrix2 == null) {
                    queue.terminate();
                    System.out.println("No more matrices to read. Producer Thread is terminating");
                    return;
                }

                MatricesPair matricesPair = new MatricesPair();
                matricesPair.matrix1 = matrix1;
                matricesPair.matrix2 = matrix2;

                queue.add(matricesPair);
            }
        }

        private float[][] readMatrix() {
            float matrix[][] = new float[N][N];
            for (int row = 0; row < N; row++) {
                if (!scanner.hasNext()) {
                    return null;
                }
                String[] line = scanner.nextLine().split(",");

                for (int column = 0; column < N; column++) {
                    matrix[row][column] = Float.valueOf(line[column]);
                }
            }
            scanner.nextLine();
            return matrix;
        }
    }

    public static class ThreadSafeQueue {
        private Queue<MatricesPair> queue = new LinkedList<>();
        private boolean isEmpty = true;
        private boolean isTerminated = false;
        private static final int CAPACITY = 5;

        public synchronized void add(MatricesPair matricesPair) {
            while (queue.size() == CAPACITY) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.add(matricesPair);
            isEmpty = false;
            notify();

        }

        public synchronized MatricesPair remove() {
            MatricesPair matricesPair = null;

            while (isEmpty && !isTerminated) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (queue.size() == 1) {
                isEmpty = true;
            }

            if (queue.size() == 0 && isTerminated) {
                return null;
            }

            System.out.println("queue size " + queue.size());

            matricesPair = queue.remove();

            if (queue.size() == CAPACITY - 1) {
                notifyAll();
            }

            return matricesPair;
        }


        public synchronized void terminate() {

            isTerminated = true;
            notifyAll();
        }
    }

    public static class MatricesPair {

        public float matrix1[][];
        public float matrix2[][];


    }
}
