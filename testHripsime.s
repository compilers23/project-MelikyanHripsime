.data

.comm a, 8
.comm b, 8
.comm c, 8


.text
.globl _start

_start:
movq $42, a
movq a, %rax
add $16, %rax
movq %rax, b
add %rax, %rbx
movq %rbx, c
sub $35, %rcx
movq %rcx, c


exit(0)
mov $60, %rax
mov c, %rdi
syscall