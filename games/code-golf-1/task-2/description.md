# Code golf - Finding the largest row or column

## Input
Provided from standard input via `System.in`. A string representing a matrix (a two dimensional array) of integers. Elements are separated by commas while rows of the matrix are separated by symbol '$'.
No value in the matrix exceeds 2^31-1. The number of elements in each row or column does not exceed 20.

## Task
Given a matrix, find the row or the column, with the largest sum modulo 13092017.
Example:

    1,1,1,0,0,0,1,1$1,0,0,0,0,0,1,1$0,0,0,0,0,0,0,1000

This effectively represents the following matrix:
    
    1 1 1 0 0 0 1    1
    1 0 0 0 0 0 1    1
    0 0 0 0 0 0 0 1000

In this example the largest row or column is the last column of the matrix, with a sum of 1002. 
The output should be therefore 1002 % 13092017 = 1002.
   
## Output
Largest sum, modulo 13092017, sent to standard output via `System.out`. No other characters expected.

Output (for the matrix above):

    1002

## Hints
Keep in mind that a 1x1 matrix is still a valid matrix, as is a row or a column vector.

## Code Golf
Solutions compete against each other, based on the number of bytes in your source file. The solution
*must* be correct. The player with the least total number of bytes over all assignments wins.