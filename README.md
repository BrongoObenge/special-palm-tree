# special-palm-tree
palm trees are the best thing that has happened to humankind
# Req Functional programming
- [x] Tuples
- [ ] Discriminated Union
- [x] Records / Class(scala)
- [x] Functions
- [x] Recursive Functions

# Req Datascience
- [x] Slope One
- [x] Devation Matrix
- [x] Update Deviation Matrix
- [x] Predict Ratings
- [ ] Top recommendation


#Simple
*Tuples*

*Discriminated Union*

*Records*

*Functions*

*Recursive Functions*


#General Info
Dataset small size =

#Recommendation Function Psuedo Code
For every userItem and userRating in the user's recommendations:
  For every diffItem that the user didn't rate (item2 ≠ item):
    add the deviation of diffItem with respect to userItem to
    the userRating of the userItem. Multiply that by the number of
      people that rated both userItem and diffItem.
      Add that to the running sum for diffItem
      Also keep a running sum for the number of people that
        rated diffItem.

Finally, for every diffItem that is in our results list, divide the total sum
of that item by the total frequency of that item and return the results

#UpdateDeviation
Item A (old rating)
Item B (new Rating)
