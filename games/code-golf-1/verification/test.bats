#!/usr/bin/env bats

@test "First test" {
  expected="$(cat test1.out)"
  run java Solution < test1.in
  [ "$expected" -eq "${lines[0]}" ]
}
