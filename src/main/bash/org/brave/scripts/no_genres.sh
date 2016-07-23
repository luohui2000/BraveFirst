#!/bin/bash
#output list of movies with no genres listed from movies.txt, 579 such patterns matched
sed -n '/no genres listed/p' movies.txt >no_genres.txt