"""
Mark Silvis
CS 1655
Assignment 2
"""

# imports
import sys
import os.path
import math


# algorithms
def average(base, test):
    averages = {}
    # average values for each item
    # key: item
    # value: average
    ave_counts = {}
    # number of ratings for each item
    # key: item
    # value: number of ratings

    # read in training file
    base = open(base, 'r')
    for line in base:
        line = line.split()
        item = float(line[1])
        rating = float(line[2])

        # if the item already exists, add the rating to the sum
        ave = averages.get(item)
        if not ave == None:
            averages[item] = ave + rating
            ave_counts[item] = ave_counts.get(item) + 1
        # if the item doesn't exists, add it
        else:
            averages[item] = rating
            ave_counts[item] = 1
    # divide each sum by the count to get the average
    for key in averages:
        averages[key] = averages.get(key)/ave_counts.get(key)

    # read in test data
    test = open(test, 'r')
    error = 0       # sum of errors between predicted and rating
    num = 0         # number of predictions
    for line in test:
        line = line.split()
        item = float(line[1])
        rating = float(line[2])
        num += 1

        # calculate error if prediction exists for item
        # use 0 for prediction if not
        try:
            error += math.pow(float(rating - averages.get(item)), 2)
        except:
            error += math.pow(rating - 0, 2)
    # calculate total rmse
    rmse = math.sqrt(error/num)
    return rmse


def user_euclidean(base, test):
    users = {}
    # stores users and their ratings
    # key: user
    # value: dict with key: item
    #                  value: rating

    # read in training file
    base = open(base, 'r')
    for line in base:
        line = line.split()
        user = float(line[0])
        item = float(line[1])
        rating = float(line[2])

        # if user already exists, add new rating
        u = users.get(user)
        if not u == None:
            users.get(user)[item] = rating

        # user doesn't exist, add them and their rating
        else:
            users[user] = {item: rating}

    # calculate similarities between users
    # stores similarities
    # key: user
    # value: list with index: other user
    #                  value: similarity
    similarities = {}
    for user, item_ratings in users.iteritems():
        # stores distances
        # index: user being compared to
        # value: distance
        distances = [None] * (len(users)+1)
        for item, rating in item_ratings.iteritems():
            for comp_user, comp_item_ratings in users.iteritems():
                if not user == comp_user:
                    if comp_item_ratings.has_key(item):
                        comp_user = int(comp_user)
                        if distances[comp_user] == None:
                            distances[comp_user] = 0.0
                        distances[comp_user] += math.pow(float(rating) - float(comp_item_ratings.get(item)), 2)
        sims = {}
        for index, distance in enumerate(distances):
            if not distance == None:
                d = math.sqrt(float(distance))
                d = 1/(1+d)
                sims[index] = d
        similarities[user] = sims

    ## print similarities
    #for key, val in similarities.iteritems():
    #    print "user: " + key
    #    print "val: " + str(val)

    # read in test file
    test = open(test, 'r')
    error = 0   # sum of errors between predicted and rating
    num = 0     # number of predictions
    for line in test:
        line = line.split()
        user = float(line[0])
        item = float(line[1])
        rating = float(line[2])
        weight_rating_sum = 0.0
        weight_sum = 0.0

        sims = similarities.get(user)
        for u in sims:
            weight = sims.get(u)
            #print "weight = " + str(weight)
            item_ratings = users.get(u)
            if item_ratings.has_key(item):
                rate = item_ratings.get(item)
                #print "rate = " + str(rate)
                weight_rate = float(weight) * float(rate)
                weight_rating_sum += weight_rate
                weight_sum += weight

        if not weight_sum == 0:        
            prediction = weight_rating_sum/weight_sum
            #print
            #print "prediction for %s, %s: %g" % (user, item, prediction)
            num += 1
            error = error + math.pow((float(rating) - prediction), 2)

    # calculate total rmse
    rmse = math.sqrt(error/num)
    return rmse

def user_pearson(base, test):
    users = {}
    # stores users and their ratings
    # key: user
    # value: dict with key: item and value: rating

    # read in training file
    base = open(base, 'r')
    for line in base:
        line = line.split()
        user = line[0]
        item = line[1]
        rating = line[2]

        # if user already exists, add new rating
        if user in users:
            item_ratings = users.get(user)
            item_ratings[item] = rating
            users[user] = item_ratings

        # user doesn't exist, add them and their rating
        else:
            item_rating = {item: rating}
            users[user] = item_rating

    # calculate similarities
    similarities = {}
    for user, item_ratings in users.iteritems():
        for comp_user, comp_item_ratings in users.iteritems():
            if not user == comp_user:
                # pearson correlation coefficient algorithm
                # [n*sum(x*y)-sum(x)*sum(y)]/[sqrt(n*sum(x^2)-sum(x)^2)*sqrt(n*sum(y^2)-sum(y)2)]
                num = 0.0     # number of common ratings
                sum_x_y = 0.0
                sum_x = 0.0
                sum_y = 0.0
                numerator = 0.0
                sum_x_squared = 0.0
                sum_y_squared = 0.0
                denominator = 0.0
                similarity = 0.0
                for item, rating in item_ratings.iteritems():
                    if comp_item_ratings.has_key(item):
                        rating = float(rating)
                        comp_rating = float(comp_item_ratings.get(item))
                        num += 1
                        sum_x_y += rating * comp_rating
                        sum_x += rating
                        sum_y += comp_rating
                        sum_x_squared += math.pow(rating, 2)
                        sum_y_squared += math.pow(comp_rating, 2)
                numerator = (num * sum_x_y) - (sum_x*sum_y)
                denominator = math.sqrt(num*sum_x_squared - math.pow(sum_x, 2)) * math.sqrt(num*sum_y_squared - math.pow(sum_y, 2))
                if not denominator == 0.0:
                    similarity = numerator/denominator

                if not similarities.has_key(user):
                    similarities[user] = {}
                similarities.get(user)[comp_user] = similarity

    # read in test file
    test = open(test, 'r')
    error = 0   # sum of errors between predicted and rating
    num = 0     # number of predictions
    for line in test:
        line = line.split()
        user = line[0]
        item = line[1]
        rating = line[2]

        numerator = 0
        denominator = 0
        for u, item_ratings in users.iteritems():
            if not user == u:
                similarity = similarities.get(user).get(u)
                if item_ratings.has_key(item):
                    rate = float(item_ratings.get(item))
                    if similarity < 0:
                        numerator += (-1*similarity)*(6-rate)
                    else:
                        numerator += similarity*rate
                    denominator += math.fabs(similarity)
        if not denominator == 0:
            prediction = numerator/denominator
            num += 1
            error += math.pow((float(rating) - prediction), 2)

    # calculate total rmse
    rmse = math.sqrt(error/num)
    return rmse

def item_cosine(base, test):
    items = {}
    # stores items and their ratings
    # key: item
    # value: dict with key: user
    #                  value: rating

    # read in training file
    base = open(base, 'r')
    for line in base:
        line = line.split()
        user = line[0]
        item = line[1]
        rating = line[2]

        # if item already exists, add new rating
        if item in items:
            user_ratings = items.get(item)
            user_ratings[user] = rating
            items[item] = user_ratings
        # item doesn't exist, add it and its rating
        else:
            user_rating = {user: rating}
            items[item] = user_rating

    # calculate similarities between items
    # stores similarities
    # key: item
    # value: list with index: other item
    #                  value: similarity

    similarities = {}
    for item, user_ratings in items.iteritems():
        for comp_item, comp_user_ratings in items.iteritems():
            if not comp_item == item:
                numerator = None
                denom1 = None
                denom2 = None
                denominator = None
                for user, rating in user_ratings.iteritems():
                    if comp_user_ratings.has_key(user):
                        if numerator == None:
                            numerator = 0.0
                            denom1 = 0.0
                            denom2 = 0.0
                        comp_rating = comp_user_ratings.get(user)
                        numerator += (float(rating) * float(comp_rating))
                        denom1 += math.pow(float(rating), 2)
                        denom2 += math.pow(float(comp_rating), 2)
                if not denom1 == None and not denom2 == None:
                    if not similarities.has_key(item):
                        similarities[item] = {}
                    denominator = math.sqrt(denom1) * math.sqrt(denom2)
                    similarities.get(item)[comp_item] = numerator/denominator


    # read in test file
    test = open(test, 'r')
    error = 0   # sum of errors between predicted and rating
    num = 0     # number of predictions
    for line in test:
        line = line.split()
        user = line[0]
        item = line[1]
        rating = line[2]
        weight_rating_sum = 0.0
        weight_sum = 0.0

        sims = similarities.get(item)
        try:    # temporary solution for Nonetype errors in dict
            for i in sims:
                weight = sims.get(i)
                #print "weight = " + str(weight)
                user_ratings = items.get(i)
                if user_ratings.has_key(user):
                    rate = user_ratings.get(user)
                    #print "rate = " + str(rate)
                    if weight >= 0:
                        weight_rate = float(weight) * float(rate)
                    else:
                        weight = weight * -1
                        weight_rate = weight * (6-rating)
                    weight_rating_sum += weight_rate
                    weight_sum += weight
        except:
            continue
        if not weight_sum == 0:        
            prediction = weight_rating_sum/weight_sum
            #print
            #print "prediction for user: {}, item: {} = ".format(user, item) + str(prediction)
            num += 1
            error = error + math.pow((float(rating) - prediction), 2)

    # calculate total rmse
    rmse = math.sqrt(error/num)
    return rmse


def item_adcosine(base, test):
    items = {}
    # stores items and their ratings
    # key: item
    # value: dict with key: user
    #                  value: rating
    # read in training file
    base = open(base, 'r')
    for line in base:
        line = line.split()
        user = line[0]
        item = line[1]
        rating = line[2]

        # if item already exists, add new rating
        if item in items:
            user_ratings = items.get(item)
            user_ratings[user] = rating
            items[item] = user_ratings
        # item doesn't exist, add it and its rating
        else:
            user_rating = {user: rating}
            items[item] = user_rating

    # print items and their ratings
    #for item, user_ratings in items.iteritems():
    #    print item
    #    for user, rating in user_ratings.iteritems():
    #        print "%s: %s" % (user, rating)

    # calculate average per user
    averages = {}
    # stores average of user's ratings
    # key: user
    # value: average
    ave_counts = {}
    # stores number of ratings
    # key: user
    # value: number of ratings
    for item, user_ratings in items.iteritems():
        for user, rating in user_ratings.iteritems():
            item = float(item)
            rating = float(rating)
            if averages.has_key(user):
                averages[user] = averages.get(user) + rating
                ave_counts[user] = ave_counts.get(user) + 1
            else:
                averages[user] = rating
                ave_counts[user] = 1
    for user in averages:
        averages[user] = averages.get(user)/ave_counts.get(user)

    # print averages
    #for key, val in averages.iteritems():
    #    print key
    #    print val

    # calculate similarities between items
    similarities = {}
    # stores similarities
    # key: item
    # value: dict with index: other item
    #                  value: similarity
    for item, user_ratings in items.iteritems():
        for comp_item, comp_user_ratings in items.iteritems():
            if not comp_item == item:
                numerator = 0
                denominator1 = 0
                denominator2 = 0

                for user, rating in user_ratings.iteritems():
                    if comp_user_ratings.has_key(user):
                        rating = float(rating)
                        comp_rating = float(comp_user_ratings.get(user))
                        numerator1 = rating - averages.get(user)
                        numerator2 = comp_rating - averages.get(user)
                        numerator += numerator1 * numerator2
                        denominator1 += math.pow(rating - averages.get(user), 2)
                        denominator2 += math.pow(comp_rating - averages.get(user), 2)

                #print "similarity between %s and %s" % (item, comp_item)
                #print "numerator: %g" % numerator
                denominator = math.sqrt(denominator1) * math.sqrt(denominator2)
                #print "denominator: %g" % denominator
                if denominator == 0:
                    break
                similarity = numerator/denominator
                #print "similarity: %g" % similarity
                if not similarities.has_key(item):
                    similarities[item] = {}
                similarities.get(item)[comp_item] = similarity

    #print "similarities"
    #for key, val in sorted(similarities.iteritems()):
    #    print key
    #    print val

    # read in test file
    test = open(test, 'r')
    error = 0   # sum of errors between predicted and rating
    num = 0     # number of predictions
    for line in test:
        line = line.split()
        user = line[0]
        item = line[1]
        rating = line[2]

        numerator = 0
        denominator = 0
        for comp_item, user_ratings in items.iteritems():
            rating = float(rating)
            if not item == comp_item:
                similarity = None
                try:
                    similarity = similarities.get(item).get(comp_item)
                except:
                    continue
                if user_ratings.has_key(user):
                    rate = float(user_ratings.get(user))
                    if similarity < 0:
                        try:
                            numerator += (-1*similarity)*(6-rate)
                        except:
                            continue
                    else:
                        numerator += similarity*rate
                    denominator += math.fabs(similarity)
        if not denominator == 0:
            prediction = numerator/denominator
            #print
            #print "prediction for user: %s, item: %s = %g" % (user, item, prediction)
            num += 1
            error += math.pow(rating - prediction, 2)

    # calculate total rmse
    rmse = math.sqrt(error/num)
    return rmse


def slope_one(base, test):
    items = {}
    # stores items and their ratings
    # key: item
    # value: dict with key: user
    #                  value: rating
    
    users = {}
    # stores users and their ratings
    # key: user
    # value: dict with key: item
    #                   value: rating

    # read in training file
    base = open(base, 'r')
    for line in base:
        line = line.split()
        user = line[0]
        item = line[1]
        rating = line[2]

        # if item already exists, add new rating
        if item in items:
            user_ratings = items.get(item)
            user_ratings[user] = rating
            items[item] = user_ratings
        # item doesn't exist, add it and its rating
        else:
            user_rating = {user: rating}
            items[item] = user_rating

        # if user already exists, add new rating
        if user in users:
            item_ratings = users.get(user)
            item_ratings[item] = rating
            users[user] = item_ratings

        # user doesn't exist, add them and their rating
        else:
            item_rating = {item: rating}
            users[user] = item_rating

    # calculate average distances
    # key: item
    # value: dict with key: other item
    #                  value: tuple (average rating, number of ratings)
    distances = {}
    #print "distances"
    #print
    for item, user_ratings in sorted(items.iteritems()):
        if not distances.has_key(item):
            distances[item] = {}
        for comp_item, comp_user_ratings in sorted(items.iteritems()):
            average_rating = 0
            ratings_count = 0
            if not item == comp_item:
                if not distances.has_key(comp_item):
                    distances[comp_item] = {}
                if distances.get(comp_item).has_key(item):
                    continue
                if distances.get(item).has_key(comp_item):
                    continue
                for user, rating in user_ratings.iteritems():
                    if comp_user_ratings.has_key(user):
                        rating = float(rating)
                        comp_rating = float(comp_user_ratings.get(user))
                        ratings_count += 1
                        average_rating += (rating - comp_rating)
            if ratings_count > 0:
                average_rating = average_rating/ratings_count
                tup = (average_rating, ratings_count)
                d = distances.get(item)
                d[comp_item] = tup
                distances[item] = d
                d = distances.get(comp_item)
                d[item] = tup
                distances[comp_item] = d

    #print "distances"
    #for key, val in distances.iteritems():
    #    print key
    #    print val

    # read in test file
    test = open(test, 'r')
    error = 0   # sum of errors between predicted and rating
    num = 0     # number of predictions
    #print "predictions"
    #print
    for line in test:
        line = line.split()
        user = line[0]
        item = line[1]
        rating = line[2]

        numer = 0.0
        denom = 0.0
        try:
            for i, tup in distances.get(item).iteritems():
                if users.get(user).has_key(i):
                    #print "(%g + %g) * %g" % (float(users.get(user).get(i)), float(tup[0]), float(tup[1]))
                    numer += ((float(users.get(user).get(i)) + float(tup[0])) * float(tup[1]))
                    denom += tup[1]
        except:
            continue
        #print "%g / %g" % (numer, denom)
        prediction = numer/denom
        #print "prediction for %s, %s = %g" % (user, item, prediction)
        num += 1
        error = error + math.pow((float(rating) - prediction), 2)

    # calculate total rmse
    rmse = math.sqrt(error/num)
    return rmse


# main
# variables
base_file = None
test_file = None
algorithm = None
rmse = None

# check arguments
if len(sys.argv) < 4:
    sys.exit("\nUsage: python myrecsys.py training test algorithm\ntraining: the training data\ntest: the test data")
else:
    base_file = sys.argv[1]
    test_file = sys.argv[2]
    algorithm = sys.argv[3]

if(not os.path.isfile(base_file)):
    sys.exit("Base file not found")

if(not os.path.isfile(test_file)):
    sys.exit("Test file not found")

# run algorithm
if algorithm == "average":
    rmse = average(base_file, test_file)

elif algorithm == "user-euclidean":
    rmse = user_euclidean(base_file, test_file)

elif algorithm == "user-pearson":
    rmse = user_pearson(base_file, test_file)

elif algorithm == "item-cosine":
    rmse = item_cosine(base_file, test_file)

elif algorithm == "item-adcosine":
    rmse = item_adcosine(base_file, test_file)

elif algorithm == "slope-one":
    rmse = slope_one(base_file, test_file)

else:
    sys.exit("""
Algorithm must be one of the following:
average
user-euclidean
user-pearson
item-cosine
item-adcosine
slope-one""")

# output
print
print "MYRESULTS Training\t= %s" % base_file
print "MYRESULTS Testing\t= %s" % test_file
print "MYRESULTS Algorithm\t= %s" % algorithm
print "MYRESULTS RMSE\t\t= %g" % rmse

sys.exit()