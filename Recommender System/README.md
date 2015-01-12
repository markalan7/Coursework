# Recommender System

Evaluates and recommends items by implementing collaborative filtering algorithms.

**Algorithms**    
Supports the following algorithms:    
- (average) Simply averaging over all ratings     
- (user-euclidean) User-based collaborative filtering using Euclidean distance metric     
- (user-pearson) User-based collaborative filtering using Pearson coefficient     
- (item-cosine) Item-based collaborative filtering using cosine similarity metric     
- (item-adcosine) Item-based collaborative filtering using adjusted cosine similarity metric     
- (slope-one) Weighted slop one collaborative filtering

**Usage**     
python myrecsys.py base_data test_data algorithm

This script will return the root mean squared error of each algorithm, not the actual recommendation. The recommendation is calculated, of course.

Sample base and test files included.

**Requires** Python 2.7