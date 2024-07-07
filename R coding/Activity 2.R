#Activity 2
#Reading the csv file:
advData <- read.csv("Advertising.csv")
pairs(advData)

#Correlation matrix of the variables
cor(advData)

#Multiple linear regression to predict Sales using TV, Radio and Newspaper
lmSales_All <- lm(Sales ~ . , data= advData)
summary(lmSales_All)

#Since p value of newspaper is greater than 0.05, we cannot reject the null for the coefficient newspaper. 
#run the prediction of Sales using TV and Radio only
lmSales_TVRadio <- lm(Sales ~ TV + Radio, data= advData)

summary(lmSales_TVRadio)

#Compare R^2 and adjusted R^2 to choose the model
#Model 1: Adjusted R-squared of 0.8956   Model 2: Adjusted R-squared of 0.8962
#95% confidence intervals for the coefficients
confint(lmSales_TVRadio)
confint(lmSales_All)        
#Regress sales with TV, Radio and TV-Radio interactive factor
lmSales_Synergy <- lm(Sales ~ TV +Radio +Radio:TV, data =advData)
summary(lmSales_Synergy)
# Model 3: R-squared of 0.9673 --> better than model 1 and 2

        