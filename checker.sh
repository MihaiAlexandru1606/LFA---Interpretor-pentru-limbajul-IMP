#!/bin/bash

# credits to AI :D
TEST_DIR=test
TEST_LIST=$TEST_DIR/tests
REF_DIR=$TEST_DIR/ref
OUT_DIR=$TEST_DIR/out
TEST_FILE=input
VARS_OUT=output
TREE_OUT=arbore
BONUS_OUT=arbore-b

print_header()
{
	header="${1}"
	header_len=${#header}
	printf "\n"
	if [ $header_len -lt 71 ]; then
		padding=$(((71 - $header_len) / 2))
		for ((i = 0; i < $padding; i++)); do
			printf " "
		done
	fi
	printf "= %s =\n\n" "${header}"
}

# Compile student homework
# comment this if you use Java or Haskell - this means that the executable should be generated before running this script
make build &> /dev/null
if [ $? -ne 0 ]; then
	echo "Makefile error!"
	exit 1
fi

tput setaf 5
print_header "Tema LFA"

# Run tests
score=0
total_score=0
paser_score=0
total_paser_score=0
interpretor_score=0
total_interpretor_score=0
antlr_score=0
total_antlr_score=0

mkdir -p $OUT_DIR $OUT_DIR/paser $OUT_DIR/stderr $OUT_DIR/stdout $OUT_DIR/antlr $OUT_DIR/interpretor   

while read test_case
do
	# Clean latest output
	rm $TEST_FILE &> /dev/null
	rm $VARS_OUT &> /dev/null
	rm $TREE_OUT &> /dev/null

	# Parse the components of the line
	test_name="$(echo $test_case | cut -d' ' -f 1)"
	tree_points="$(echo $test_case | cut -d' ' -f 2)"
	vars_points="$(echo $test_case | cut -d' ' -f 3)"
	input="$(echo $test_case | cut -d' ' -f 4)"
	vars_ref="$(echo $test_case | cut -d' ' -f 5)"
	tree_ref="$(echo $test_case | cut -d' ' -f 6)"

	# Create a link to the current test file
	ln -s $TEST_DIR/in/$input $TEST_FILE

	# Update scores
	total_score=$(($total_score + $tree_points + $vars_points))
	total_paser_score=$(($total_paser_score + $tree_points))
	total_interpretor_score=$((total_interpretor_score + $vars_points))	
	total_antlr_score=$(($total_antlr_score + $tree_points))	

	# Run the student homework
	make -s run 2> .error 1> .stdout
	if [ $? -ne 0 ]
	then
		tput sgr0		
		cat .error
		echo "\n"		
		cp .error $OUT_DIR/stderr/$test_name.error 		
		continue
	fi
	
	tput setaf 6
	echo -ne "Test : $test_name\n"
	cp $TREE_OUT $OUT_DIR/paser/$test_name.arbore &> /dev/null
	cp $VARS_OUT $OUT_DIR/interpretor/$test_name.vars &> /dev/null
	cp $BONUS_OUT $OUT_DIR/antlr/$test_name.antlr &> /dev/null
	cp .error $OUT_DIR/stderr/$test_name.error &> /dev/null
	cp .stdout $OUT_DIR/stdout/$test_name.stdout &> /dev/null
	
	# Compare parsing results
	cmp -s $TREE_OUT $REF_DIR/$tree_ref

	if [ $? -eq 0 ]
	then
		tput setaf 2	
		echo -ne "\t[PARSER].......................................PASSED\n";
		score=$(($score + $tree_points))
		paser_score=$(($paser_score + $tree_points))
	else
		tput setaf 1
		echo -ne "\t[PARSER].......................................FAILED\n"
		echo "official output -> "$REF_DIR/$tree_ref
	fi

	# Compare interpreting results
	cmp -s $VARS_OUT $REF_DIR/$vars_ref

	if [ $? -eq 0 ]
	then
		tput setaf 2
		echo -ne "\t[INTERPRETOR]..................................PASSED\n"
		score=$(($score + $vars_points))
		interpretor_score=$(($interpretor_score + $vars_points)) 
	else
		tput setaf 1
		echo -ne "\t[INTERPRETOR]..................................FAILED\n" 
		echo "official output -> "$REF_DIR/$vars_ref
	fi

	# bonus
	cmp -s $BONUS_OUT $REF_DIR/$tree_ref

	if [ $? -eq 0 ]
	then
		tput setaf 2	
		echo -ne "\t[BONUS]........................................PASSED\n";
		score=$(($score + $tree_points))
		antlr_score=$(($antlr_score + $tree_points))
	else
		tput setaf 1
		echo -ne "\t[BONUS]........................................FAILED\n"
	fi
	

done < $TEST_LIST

rm $TEST_FILE &> /dev/null
rm $VARS_OUT &> /dev/null
rm $TREE_OUT &> /dev/null
rm $BONUS_OUT &> /dev/null
rm .error .stdout &> /dev/null
make clean &> /dev/null

tput setaf 4
echo -ne "\n\n"
echo "Paser points.........................................$paser_score\\$total_paser_score"
echo "Interpretor points...................................$interpretor_score\\$total_interpretor_score"
echo "Bonus points.........................................$antlr_score\\$total_antlr_score"
echo "Total points.........................................$score\\$total_score"
