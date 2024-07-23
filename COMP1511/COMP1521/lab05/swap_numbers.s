# read 10 numbers into an array
# swap any pairs of of number which are out of order
# then print the 10 numbers

# i in register $t0,
# registers $t1 - $t3 used to hold temporary results

main:

    li   $t0, 0         # i = 0
loop0:
    bge  $t0, 10, end0  # while (i < 10) {

    li   $v0, 5         #   scanf("%d", &numbers[i]);
    syscall             #

    mul  $t1, $t0, 4    #   calculate &numbers[i]
    la   $t2, numbers   #   $t2 = &numbers
    add  $t3, $t1, $t2  #   $t3 = &numbers[i]
    sw   $v0, ($t3)     #   store entered number in array

    addi $t0, $t0, 1    #   i++;
    j    loop0          # }
end0:
    
    li   $t0, 1         # i = 1
loop1:
    bge  $t0, 10, end1  # while (i < 10) {

    mul  $t1, $t0, 4    #   calculate &numbers[i]
    la   $t2, numbers   #   $t2 = &numbers
    add  $t3, $t1, $t2  #   $t3 = &numbers[i]
    lw   $t4, ($t3)     #   $t4 = x = numbers[i]

    sub  $t1, $t3, 4    #   $t1 = &numbers[i - 1]
    lw   $t5, ($t1)     #   $t5 = y = numbers[i - 1]

    bge  $t4, $t5, skip 

    mul  $t1, $t0, 4    #   calculate &numbers[i]
    la   $t2, numbers   #   $t2 = &numbers
    add  $t3, $t1, $t2  #   $t3 = &numbers[i]
    sw   $t5, ($t3)     #   $t4 = x = numbers[i]

    sub  $t1, $t3, 4    #   $t1 = &numbers[i - 1]
    sw   $t4, ($t1)

skip:
    addi $t0, $t0, 1    # i++;
    j    loop1

end1:
    li   $t0, 0         # i = 0;

loop2:
    bge  $t0, 10, main_end  # while (i < 10) {

    mul  $t1, $t0, 4    #   calculate &numbers[i]
    la   $t2, numbers   #
    add  $t3, $t1, $t2  #
    lw   $a0, ($t3)     #   load numbers[i] into $a0
    li   $v0, 1         #   printf("%d", numbers[i])
    syscall

    li   $a0, '\n'      #   printf("%c", '\n');
    li   $v0, 11
    syscall

    addi $t0, $t0, 1    #   i++
    j    loop2          # }
main_end:
    li   $v0, 0
    jr   $ra            # return 0

.data

numbers:
    .word 0 0 0 0 0 0 0 0 0 0  # int numbers[10] = {0};

