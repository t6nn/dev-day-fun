#!/usr/bin/env bats

@test "Simple array" {
  expected="$(cat test1.out)"
  run java -cp ../out FindMatrixSum < test1.in
  [ "$expected" = "${lines[0]}" ]
}

@test "Larger values" {
  expected="$(cat test2.out)"
  run java -cp ../out FindMatrixSum < test2.in
  [ "$expected" = "${lines[0]}" ]
}

@test "Row vector" {
  expected="$(cat test3.out)"
  run java -cp ../out FindMatrixSum < test3.in
  [ "$expected" = "${lines[0]}" ]
}

@test "Column vector" {
  expected="$(cat test4.out)"
  run java -cp ../out FindMatrixSum < test4.in
  [ "$expected" = "${lines[0]}" ]
}