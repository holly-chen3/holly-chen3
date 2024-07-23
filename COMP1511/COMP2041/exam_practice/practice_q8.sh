#!/bin/dash

for file1 in "$@"
do
    files="$files $file1 "
    for file2 in "$@"
    do
        case "$files" in
            *" $file2 "*)
                continue
                ;;
        esac
        diff "$file1" "$file2" >/dev/null || continue
        echo "ln -s $file1 $file2"
        files="$files $file2 "
        printed=1
    done
done

test -z "$printed" && echo "No files can be replaced by symbolic links"