
grep 'Result: ' $1 | sed 's/^.*: //' > $1.csv
