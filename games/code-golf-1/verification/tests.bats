#!/usr/bin/env bats

@test "First test" {
  expected="$(cat test1.out)"
  run java -cp ../che-workspace/out Solution < test1.in
  [ "$expected" = "${lines[0]}" ]
}
