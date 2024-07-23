#!/bin/dash

year=$1
course=$2

if [ $# -eq 2 ]
then
  if [ "$year" -eq "$year" ] 2> /dev/null && [ "$year" -ge 2019 ] 2> /dev/null && [ "$year" -le 2023 ] 2> /dev/null
  then
    curl -sL "https://www.handbook.unsw.edu.au/api/content/render/false/query/+unsw_psubject.implementationYear:${year}%20+unsw_psubject.studyLevel:undergraduate%20+unsw_psubject.educationalArea:${course}*%20+unsw_psubject.active:1%20+unsw_psubject.studyLevelValue:ugrd%20+deleted:false%20+working:true%20+live:true/orderby/unsw_psubject.code%20asc/limit/10000/offset/0" "https://www.handbook.unsw.edu.au/api/content/render/false/query/+unsw_psubject.implementationYear:${year}%20+unsw_psubject.studyLevel:postgraduate%20+unsw_psubject.educationalArea:${course}*%20+unsw_psubject.active:1%20+unsw_psubject.studyLevelValue:pgrd%20+deleted:false%20+working:true%20+live:true/orderby/unsw_psubject.code%20asc/limit/10000/offset/0" |
    jq -r ".contentlets | .[] | [.code, .title] | @tsv" | sed -E 's/\s+/ /g;s/\s+$//' | sort | uniq
    exit 0
  else
    echo "$0: argument 1 must be an integer between 2019 and 2023"
    exit 1
  fi
else
  echo "Usage: $0 <year> <course-prefix>"
  exit 1
fi
