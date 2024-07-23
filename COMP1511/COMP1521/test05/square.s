main:
    li   $v0, 5         #   scanf("%d", &x);
    syscall             #
    move $t0, $v0       #   $t0 = x

    li   $t1, 0         #   $t1 = i = 0

loop1: 
    bge  $t1, $t0, end  #   while(i < x) {
    li   $t2, 0         #   $t2 = j = 0
    
loop2:                  #   while(j < x) {
    bge  $t2, $t0, loop1_end
 
    li   $a0, '*'       #   printf("%c", '\n');
    li   $v0, 11
    syscall

    addi $t2, $t2, 1    #   j = j + 1

    b    loop2
loop1_end:
    addi $t1, $t1, 1    #   i = i + 1

    li   $a0, '\n'       #   printf("%c", '\n');
    li   $v0, 11
    syscall

    b    loop1
end:

    li   $v0, 0         # return 0
    jr   $ra
