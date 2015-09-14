package com.iconic.test;


/*
---------- Q1 -----------

The nth 'Factorial' F(n) for positive integers n is defined by:
	
F(0) = 1
F(n) = 1*2*3*...*(n-1)*n
	
So:
F(2) = 1*2 = 2
F(3) = 1*2*3 = 6
etc.

(1a): Write an iterative (non-recursive) function to compute F(n) 
(1a): Write a recursive function to compute F(n)

*/

public class Q1 {
	public static void main(String[] args) {
		int arg = 3;
		int result1 = iterative(arg);
		int result2 = recursive(arg);
		
		System.out.println("iterative result: " + result1);
		System.out.println("recursive result: " + result2);
	}
	public static int iterative(int x) {
		if (x < 0) {
			throw new IllegalArgumentException("x must be >= 0");
		}
		int fact = 1;
		for (int i = 2; i <= x; i++) {
			fact *= i;
		}
		return fact;
	}

	public static int recursive(int x) {
		if (x < 0) {
			throw new IllegalArgumentException("x must be >= 0");
		}
		if (x <= 1) {
			return 1;
		} else
			return x * recursive(x - 1);
	}
}
