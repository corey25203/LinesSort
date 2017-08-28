
java -XX:+UseG1GC -XX:+UseStringDeduplication -XX:StringDeduplicationAgeThreshold=3 -Dfile.encoding=UTF-8 com.company.Main test.txt result.txt 1
pause