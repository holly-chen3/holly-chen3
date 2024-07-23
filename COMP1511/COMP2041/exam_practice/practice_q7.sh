#!/bin/dash

for fileName in "$@"
do
    if [ -e "$fileName" ] 
    then
        read -r firstLine < "$fileName"
    fi
    if echo "$fileName" | grep -q "\." >/dev/null
    then
        echo "# $fileName already has an extension"
    elif ! echo "$firstLine" | grep -E '^#!' >/dev/null
    then
        echo "# $fileName does not have a #! line"
    elif ! echo "$firstLine" | grep -E '(perl|python|sh)' >/dev/null
    then
        echo "# $fileName no extension for #! line"
    else
        newFileName=""
        if echo "$firstLine" | grep -E 'perl' >/dev/null
        then
            newFileName="$fileName.pl"
        elif echo "$firstLine" | grep -E 'python' >/dev/null
        then
            newFileName="$fileName.py"
        elif echo "$firstLine" | grep -E 'sh' >/dev/null
        then
            newFileName="$fileName.sh"
        fi
        if [ -e "$newFileName" ]
        then
            echo "# $newFileName already exists"
        else
            echo "mv $fileName $newFileName"
        fi
    fi
done
exit 0
