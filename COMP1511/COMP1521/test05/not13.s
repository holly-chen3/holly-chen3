main:
    li   $v0, 5         #   scanf("%d", &x);
    syscall             #
    move $t0, $v0       #   $t0 = x

    li   $v0, 5         #   scanf("%d", &y);
    syscall             #
    move $t1, $v0       #   $t1 = y

    addi $t2, $t0, 1    #   $t2 = x + 1 = i
loop:
    bge  $t2, $t1, end

    beq  $t2, 13, skip

    move $a0, $t2       #   printf("%d\n", i);
    li   $v0, 1
    syscall
    li   $a0, '\n'      #   printf("%c", '\n');
    li   $v0, 11
    syscall
skip:
    addi $t2, $t2, 1
    b    loop
end:

    li   $v0, 0         # return 0
    jr   $ra
