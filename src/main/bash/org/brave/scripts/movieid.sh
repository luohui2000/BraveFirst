
#!/bin/bash
echo "=========================$0 starts!======================"

while read LINE
 do 
 echo ${LINE%%,*} >> movieids.txt
done < movies.txt
