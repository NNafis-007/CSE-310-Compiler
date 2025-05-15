#!/bin/bash

#compile .l

if [[ $# -lt 2 ]]; then
    echo "USAGE : test_lex.sh <.l_file> <input.txt>"
    exit 1
fi

lex_file_name=$1
flex $lex_file_name

#complie lex.yy.c
g++ lex.yy.c

#run a.out
input_txt_file=$2
./a.out $input_txt_file