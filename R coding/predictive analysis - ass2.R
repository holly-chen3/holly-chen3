library(rpart)
library(rpart.plot)
data <- read.csv("z5363886_z5363886-Assessment2Data.csv")
data <- na.omit(data)

ndata <- nrow(data)
set.seed(1)  #Set the seed so that we always get the same results
train <- sample(ndata, ndata*0.8)
dataTrain <- data[train, ]
dataTest <- data[-train, ]
dim(dataTrain)
dim(dataTest)

# Monthly Income
lmArea <- lm(C_TotalVoluntarySuperContribution ~ C_MonthlyIncome, data = dataTrain)
summary(lmArea)
coef(lmArea)
confint(lmArea)

plot(dataTrain$C_MonthlyIncome, dataTrain$C_TotalVoluntarySuperContribution, 
     xlab = "Monthly Income", ylab = "Total Voluntary Superannuation Contribution", main = "Train")
abline(lmArea, col = "blue", lwd = 2)

summary(lmArea)$r.squared


lmSalary <-  lm(C_TotalVoluntarySuperContribution ~ C_SalarySacrifice, data = dataTrain)
summary(lmSalary)



# GLM for app satisfaction rating
dataTrain$App_SatisfactionRating2 <- ifelse(dataTrain$App_SatisfactionRating == "H", 1, 0)
lmHome <-  glm(dataTrain$App_SatisfactionRating2 ~ C_HomeOwnershipStatus
              +C_FinancialLiteracy+App_ShowTutorial, family = binomial(), data = dataTrain)
summary(lmHome)

#Make predictions
probs <- predict(lmHome, newdata = dataTrain, type = "response") 
dim(dataTrain)
probsPredLogistic <- rep("L", 1200)
probsPredLogistic[probs > 0.5] <- "H" 

#Confusion matrix
table(probsPredLogistic, dataTrain$App_SatisfactionRating)
(402+609)/1200
402/(402+112)
609/(77+609)

#Make predictions
probsTest <- predict(lmHome, newdata = dataTest, type = "response") 
dim(dataTest)
probsPredLogisticTest <- rep("L", 300)
probsPredLogisticTest[probsTest > 0.5] <- "H" 

#Confusion matrix
table(probsPredLogisticTest, dataTest$App_SatisfactionRating)
(96+160)/300
96/(96+21)
160/(23+160)
# Classification tree for app satisfaction rating
treeFruit <- rpart(App_SatisfactionRating ~ C_HomeOwnershipStatus
                   +C_FinancialLiteracy+App_ShowTutorial, data = dataTrain)

rpart.plot(treeFruit)

#Make predictions
predTree <- predict(treeFruit, dataTrain, type = "class")

#Compute the confusion matrix
table(predTree, dataTrain$App_SatisfactionRating)
(401+614)/1200
401/(401+113)
614/(72+614)

#Make predictions
testPredTree <- predict(treeFruit, dataTest, type = "class")

#Compute the confusion matrix
table(testPredTree, dataTest$App_SatisfactionRating)
(99 + 162)/300
99/(99+18)
162/(21+162)