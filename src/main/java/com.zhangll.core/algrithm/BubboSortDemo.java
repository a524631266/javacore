package com.zhangll.core.algrithm;

public class BubboSortDemo {
    public static void main(String[] args) {
        int[] arr =new int[]{1,5,5,8,6,2,4,2,10,11,23,54,341,123,123,35,5,8};
        bubboSort(arr);
        print(arr);
    }
    public static void bubboSort(int[] arr){
        int size = arr.length;
        int iterCount = 1;
        for (int i = 0; i < size; i++) {
            for(int j = 0 ; j < size - iterCount; j ++){
                if(arr[j] > arr[j + 1]){
                    swap(arr, j, j + 1);
                }
            }
            iterCount += 1;
        }
    }

    public static void swap(int[] arr, int i, int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    public static void print(int[] arr) {
        System.out.println(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }
}
