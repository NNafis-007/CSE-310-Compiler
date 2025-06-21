#!/usr/bin/env python3
"""
File Difference Checker
Compares two files by non-empty content lines, ignoring newlines and blank lines.
Reports mismatched content with line numbers.
"""

import sys
import os

def normalize_line(line):
    """Strip trailing whitespace and ignore blank lines"""
    stripped = line.strip()
    return stripped if stripped != '' else None

def read_file_lines(filepath):
    """Read file and return list of normalized, non-empty lines"""
    try:
        with open(filepath, 'r', encoding='utf-8') as file:
            return [normalized for line in file if (normalized := normalize_line(line)) is not None]
    except FileNotFoundError:
        print(f"Error: File '{filepath}' not found.")
        sys.exit(1)
    except Exception as e:
        print(f"Error reading file '{filepath}': {e}")
        sys.exit(1)

def compare_files(file1_path, file2_path):
    """Compare two files and report differences"""
    print(f"Comparing files:")
    print(f"  File 1: {file1_path}")
    print(f"  File 2: {file2_path}")
    print("-" * 60)
    
    # Read and normalize lines
    lines1 = read_file_lines(file1_path)
    lines2 = read_file_lines(file2_path)
    
    max_lines = max(len(lines1), len(lines2))
    differences_found = False
    
    for i in range(max_lines):
        line_num = i + 1
        line1 = lines1[i] if i < len(lines1) else ""
        line2 = lines2[i] if i < len(lines2) else ""
        
        if line1 != line2:
            differences_found = True
            print(f"Mismatch at content line {line_num}:")
            print(f"  File 1: '{line1}'" if i < len(lines1) else f"  File 1: <EOF>")
            print(f"  File 2: '{line2}'" if i < len(lines2) else f"  File 2: <EOF>")
            print()
    
    if not differences_found:
        print("✓ Files are identical (ignoring empty lines and newline differences)")
    else:
        print("✗ Files differ")
        print(f"Total non-empty lines - File 1: {len(lines1)}, File 2: {len(lines2)}")


def main():
    """Main function to handle command line arguments"""
    if len(sys.argv) != 3:
        print("Usage: python3 diffCheck.py <file1> <file2>")
        sys.exit(1)
    
    file1_path = sys.argv[1]
    file2_path = sys.argv[2]
    
    if not os.path.exists(file1_path):
        print(f"Error: File '{file1_path}' does not exist.")
        sys.exit(1)
    if not os.path.exists(file2_path):
        print(f"Error: File '{file2_path}' does not exist.")
        sys.exit(1)
    
    compare_files(file1_path, file2_path)
    

if __name__ == "__main__":
    main()
    
