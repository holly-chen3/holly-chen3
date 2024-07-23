# Sieve of Eratosthenes
# https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
main:

    li      $t0, 0                  # $t0 = i = 0
loop1:
    bge     $t0, 1000, loop1_end    # while (i < 1000) {

    la      $t1, prime
    add     $t2, $t1, $t0           #   $t2 = &prime[i]
    li      $t3, 1
    sb      $t3, ($t2)              #   prime[i] = 1;
    addi    $t0, $t0, 1             #   i++;
    
    b       loop1                   # }

loop1_end:
    li      $t0, 2                  # $t0 = i = 2
loop2:
    bge     $t0, 1000, loop2_end    # while (i < 1000) {
    
    la      $t1, prime
    add     $t2, $t1, $t0           #   $t2 = &prime[i]

    lb      $t3, ($t2)              #   $t3 = prime[i]
    beq     $t3, 0, skip            #   if (prime[i]) {
    
    move    $a0, $t0                #       printf("%d\n", i);
    li      $v0, 1
    syscall
    
    li      $a0, '\n'
    li      $v0, 11
    syscall

    mul     $t1, $t0, 2             #       $t1 = j = 2 * i;
loop3:
    bge     $t1, 1000, skip         #       while (j < 1000) {

    la      $t2, prime
    add     $t2, $t2, $t1           #           $t2 = &prime[j]
    li      $t3, 0
    sb      $t3, ($t2)                #           prime[j] = 0;

    add     $t1, $t1, $t0           #           j = j + i;

    b       loop3                   #       }
    
skip:
    addi    $t0, $t0, 1             #   i++;
    b       loop2                   # }
    
loop2_end:
    li      $v0, 0                  # return 0
    jr      $ra
.data
prime:
    .space 1000