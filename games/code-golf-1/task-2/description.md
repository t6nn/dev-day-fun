# Code golf - Finding the largest row or column

## Input
A string representing a matrix (a two dimensional array) of integers. Elements are separated by commas while rows of the matrix are separated by symbol '$'.
The sum of any row or column does not exceed 2^63-1

## Task
Given a matrix, find the row or the column, with the largest sum modulo 13092017.
Example:

    1,1,1,0,0,0,1,1$1,0,0,0,0,0,1,1$0,0,0,0,0,0,0,1000

In this example the largest row or column is the last column of the matrix, with a sum of 1002. 
The output should be therefore 1002 % 13092017 = 1002.

Output:

    1002
    
## Output
Largest sum, modulo 13092017.

## Code Golf
Solutions compete against each other, based on the number of bytes in your source file. The solution
*must* be correct. The player with the least total number of bytes over all assignments wins.