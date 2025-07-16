.MODEL SMALL
.STACK 1000H
.Data
	number DB "00000$"
	a DW 1 DUP (0000H)
	b DW 1 DUP (0000H)
	c DW 1 DUP (0000H)
.CODE
func_a PROC
	PUSH BP
	MOV BP, SP
	MOV AX, 7       ; Line 4
	MOV a, AX
	PUSH AX
	POP AX
L1:
L2:
	POP BP
	RET 
func_a ENDP
bar PROC
	PUSH BP
	MOV BP, SP
	MOV AX, [BP+8]       ; Line 9
	MOV CX, AX
	MOV AX, 4       ; Line 9
	CWD
	MUL CX
	PUSH AX
	MOV AX, [BP+6]       ; Line 9
	MOV CX, AX
	MOV AX, 2       ; Line 9
	CWD
	MUL CX
	PUSH AX
	POP AX       ; Line 9
	MOV DX, AX
	POP AX       ; Line 9
	ADD AX, DX
	PUSH AX
	MOV AX, [BP+4]       ; Line 9
	MOV DX, AX
	POP AX       ; Line 9
	ADD AX, DX
	PUSH AX
	POP AX       ; Line 9
	MOV c, AX
	PUSH AX
	POP AX
L3:
	MOV AX, c       ; Line 10
	JMP L5
L4:
L5:
	POP BP
	RET 6
bar ENDP
main PROC
	MOV AX, @DATA
	MOV DS, AX
	PUSH BP
	MOV BP, SP
	SUB SP, 2
	SUB SP, 2
	SUB SP, 2
	SUB SP, 2
L6:
	MOV AX, 5       ; Line 17
	MOV [BP-2], AX
	PUSH AX
	POP AX
L7:
	MOV AX, 6       ; Line 18
	MOV [BP-4], AX
	PUSH AX
	POP AX
L8:
	MOV AX, [BP-2]       ; Line 26
	PUSH AX
	MOV AX, [BP-4]       ; Line 26
	PUSH AX
	MOV AX, [BP-6]       ; Line 26
	PUSH AX
	CALL bar
	PUSH AX
	POP AX       ; Line 26
	MOV [BP-8], AX
	PUSH AX
	POP AX
L9:
	MOV AX, [BP-8]       ; Line 27
	CALL print_output
	CALL new_line
L10:
	MOV AX, 0       ; Line 33
	JMP L12
L11:
L12:
	ADD SP, 8
	POP BP
	MOV AX,4CH
	INT 21H
main ENDP
;-------------------------------
;         print library         
;-------------------------------
new_line proc
    push ax
    push dx
    mov ah,2
    mov dl,0Dh
    int 21h
    mov ah,2
    mov dl,0Ah
    int 21h
    pop dx
    pop ax
    ret
    new_line endp
print_output proc  ;print what is in ax
    push ax
    push bx
    push cx
    push dx
    push si
    lea si,number
    mov bx,10
    add si,4
    cmp ax,0
    jnge negate
    print:
    xor dx,dx
    div bx
    mov [si],dl
    add [si],'0'
    dec si
    cmp ax,0
    jne print
    inc si
    lea dx,si
    mov ah,9
    int 21h
    pop si
    pop dx
    pop cx
    pop bx
    pop ax
    ret
    negate:
    push ax
    mov ah,2
    mov dl,'-'
    int 21h
    pop ax
    neg ax
    jmp print
    print_output endp
;-------------------------------
END main