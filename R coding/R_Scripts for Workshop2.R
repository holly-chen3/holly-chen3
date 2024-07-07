#reading weekly sales flavours
data <- read.csv("weekly_sales_flavours.csv")
#Calculating the number of rows and columns
nrows <- nrow(data)
ncols <- ncol(data)
nrows
## [1] 52
ncols
## [1] 19
# selecting staff sales using subset function
staff <- subset(data, Customers=="Staff")

#grouping flavour by groups (namely fruit, caramel, chocolate, tea/coffee,nut )
staff_fruit <- subset(staff,select = c(Apricot,Banana,Cherry.Almond,Ginger,Lime.Coconut,Mango,Pure.Coconut,Red.Bean))
staff_caramel <- subset(staff,select = S.Caramel)
staff_chocolate <- subset(staff,select = c(Chocolate,Mint.Choco))
staff_tea_coffee <- subset(staff,select = c(Chai.Tea,Green.Tea,Coffee))
staff_nut <- subset(staff,select = c(Hazelnut,Pistachio))

# aggregate sales for each group by summing 26 weeks' sales per group 
staff_fruit_sum <- sum(staff_fruit)
staff_caramel_sum <- sum(staff_caramel)
staff_chocolate_sum <- sum(staff_chocolate)
staff_tea_coffee_sum <- sum(staff_tea_coffee)
staff_nut_sum <- sum(staff_nut)

#printing the aggregate sales
staff_fruit_sum
## [1] 22687.15
staff_caramel_sum
## [1] 10196.21
staff_chocolate_sum
## [1] 12406.76
staff_tea_coffee_sum
## [1] 9588.727
staff_nut_sum
## [1] 4166.212
# renaming the columns
staff_flavour_sales <- c(staff_fruit_sum, staff_caramel_sum, staff_chocolate_sum, staff_tea_coffee_sum, staff_nut_sum)
col_names <- c("Fruit", "Caramel", "Chocolate", "Tea/coffee", "Nut")
staff_flavour_sales
## [1] 22687.151 10196.212 12406.758  9588.727  4166.212
col_names
## [1] "Fruit"      "Caramel"    "Chocolate"  "Tea/coffee" "Nut"
#bar chart( Staff_flavour is the new file name, las is labelling, las =2 means x labelling is vertical, col represents colour, ylim is the range of y values, names.org is the column names, mar is the bottom, left, top and right margins
barplot(staff_flavour_sales, las = 2, col = "lightblue", ylim = c(0,25000), names.arg = col_names,mar =c(4,4,4,4))
title(main = "Staff Sales - Flavour Groups")

## Repeating the Activity for student
# selecting student sales
student <- subset(data, Customers=="Student")


#grouping flavour for students by groups (namely fruit, caramel, chocolate, tea/coffee,nut )
student_fruit <- subset(student,select = c(Apricot,Banana,Cherry.Almond,Ginger,Lime.Coconut,Mango,Pure.Coconut,Red.Bean))
student_caramel <- subset(student,select = S.Caramel)
student_chocolate <- subset(student,select = c(Chocolate,Mint.Choco))
student_tea_coffee <- subset(student,select = c(Chai.Tea,Green.Tea,Coffee))
student_nut <- subset(student,select = c(Hazelnut,Pistachio))

# aggregate sales for each group by summing 26 weeks' sales per group 
student_fruit_sum <- sum(student_fruit)
student_caramel_sum <- sum(student_caramel)
student_chocolate_sum <- sum(student_chocolate)
student_tea_coffee_sum <- sum(student_tea_coffee)
student_nut_sum <- sum(student_nut)

#printing the aggregate sales
student_fruit_sum
## [1] 47936.98
student_caramel_sum
## [1] 25123.79
student_chocolate_sum
## [1] 28663.38
student_tea_coffee_sum
## [1] 24428.67
student_nut_sum
## [1] 10474.67
# renaming the columns
student_flavour_sales <- c(student_fruit_sum, student_caramel_sum, student_chocolate_sum, student_tea_coffee_sum, student_nut_sum)
col_names <- c("Fruit", "Caramel", "Chocolate", "Tea/coffee", "Nut")
student_flavour_sales
## [1] 47936.98 25123.79 28663.38 24428.67 10474.67
col_names
## [1] "Fruit"      "Caramel"    "Chocolate"  "Tea/coffee" "Nut"
#bar chart
barplot(student_flavour_sales, las = 2, col = "green", ylim = c(0,55000), names.arg = col_names,mar =c(4,4,4,4))
title(main = "Student Sales - Flavour Groups")

#Alternate to two bar charts : grouped bar charts
barplot(matrix(c(staff_fruit_sum, student_fruit_sum, staff_caramel_sum , student_caramel_sum, staff_chocolate_sum, student_chocolate_sum, staff_tea_coffee_sum , student_tea_coffee_sum, staff_nut_sum, student_nut_sum ), nr = 2 ) , beside=T, col = c("red" , "blue"), ylim = c(0,50000), names.arg = col_names)
title(main = "Staff vs Student Sales - Flavour Groups")
legend("topright",c("Staff", "Students"), col = c("red", "blue"), pch = c(15,15))

#Create a new bar graph calculating averages.

staff_fruit_ave <- staff_fruit_sum/length(staff_fruit)
staff_caramel_ave <- staff_caramel_sum/length(staff_caramel)
staff_chocolate_ave <- staff_chocolate_sum/length(staff_chocolate)
staff_tea_coffee_ave <- staff_tea_coffee_sum/length(staff_tea_coffee)
staff_nut_ave <- staff_nut_sum/length(staff_nut)

top_staff_ave <- c(staff_fruit_ave, staff_caramel_ave, staff_chocolate_ave, staff_tea_coffee_ave, staff_nut_ave)
col_names <- c("fruit","caramel","choco", "coffee","nut")
options(repr.plot.width = 2, repr.plot.height = 3)
par(mar = c(5, 4, 4, 1), cex = 0.65)
barplot(unlist(top_staff_ave), las = 2, col = "lightblue", names.arg = col_names,ann =FALSE)
title(main = "Staff Average Sales - Flavour Groups")
