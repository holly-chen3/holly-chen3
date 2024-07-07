data <- read.csv("z5363886_z5363886-Assessment2Data.csv")
data <- na.omit(data)

# Total Voluntary Super Contribution frequency
hist(data$C_TotalVoluntarySuperContribution, breaks=seq(0,8000,l=17), ann=FALSE, xlim=c(0,8000), ylim=c(0,600))
title(main="Total Voluntary Super Contribution Frequency", xlab="Total Super Contribution Amount ($)", ylab="Number of users")
mean(data$C_TotalVoluntarySuperContribution)

# Age
plot(data$C_Age, data$C_TotalVoluntarySuperContribution, pch = 20, frame = FALSE, ann = FALSE)
title(xlab = "Age", ylab = "Total Voluntary Super Contribution ($)", main = "Age - Total Voluntary Super Contribution relationship")

lines(lowess(data$C_Age, data$C_TotalVoluntarySuperContribution), col = "red")
cor(data$C_Age, data$C_TotalVoluntarySuperContribution)

Age_1 <- subset(data, C_Age<=20)
Age_2 <- subset(data,C_Age>=21 & C_Age <=30)
Age_3 <- subset(data,C_Age>=31 & C_Age <=40)
Age_4 <- subset(data,C_Age>=41 & C_Age <=50)
Age_5 <- subset(data,C_Age>=51 & C_Age <=60)
Age_1_cont <- mean(Age_1$C_TotalVoluntarySuperContribution)
Age_2_cont <- mean(Age_2$C_TotalVoluntarySuperContribution)
Age_3_cont <- mean(Age_3$C_TotalVoluntarySuperContribution)
Age_4_cont <- mean(Age_4$C_TotalVoluntarySuperContribution)
Age_5_cont <- mean(Age_5$C_TotalVoluntarySuperContribution)
Age <- c(Age_1_cont,Age_2_cont,Age_3_cont,Age_4_cont,Age_5_cont)
col_names <- c("18-20","21-30","31-40","41-50","51-60")
x <- barplot(Age, las = 1, col = "lightblue", ylim = c(0,3000), names.arg = col_names,mar =c(4,4,4,4), space =1)
title(main = "Age - Total Voluntary Super Contribution relationship", xlab = "Age", ylab = "Total Voluntary Super Contribution ($)")
y<- as.matrix(ceiling(Age))
text(x,y+4,labels=as.character(y))

# Monthly Income
plot(data$C_MonthlyIncome, data$C_TotalVoluntarySuperContribution, pch = 20, frame = FALSE, ann = FALSE)
title(xlab = "Monthly Income ($)", ylab = "Total Voluntary Super Contribution ($)", main = "Monthly Income - Total Voluntary Super Contribution relationship")

lines(lowess(data$C_MonthlyIncome, data$C_TotalVoluntarySuperContribution), col = "red")

cor(data$C_MonthlyIncome, data$C_TotalVoluntarySuperContribution)
sd(data$C_MonthlyIncome)
sd(data$C_TotalVoluntarySuperContribution)


Inc_1 <- subset(data, C_MonthlyIncome<=4000)
Inc_2 <- subset(data,C_MonthlyIncome>4000 & C_MonthlyIncome <=11000)
Inc_3 <- subset(data,C_MonthlyIncome>11000 & C_MonthlyIncome <=18000)
Inc_4 <- subset(data,C_MonthlyIncome>18000 & C_MonthlyIncome <=25000)
Inc_1_cont <- mean(Inc_1$C_TotalVoluntarySuperContribution)
Inc_2_cont <- mean(Inc_2$C_TotalVoluntarySuperContribution)
Inc_3_cont <- mean(Inc_3$C_TotalVoluntarySuperContribution)
Inc_4_cont <- mean(Inc_4$C_TotalVoluntarySuperContribution)
Inc <- c(Inc_1_cont,Inc_2_cont,Inc_3_cont,Inc_4_cont)
col_names <- c("$4000","$4001-11000","$11001-18000","$18001-25000")
x <- barplot(Inc, las = 1, col = "lightblue", ylim = c(0,3500), names.arg = col_names,mar =c(4,4,4,4), space =1)
title(main = "Contribution by Income Groups", xlab=mtext("Monthly Income Groups", side=1, line=3), ylab= "Total Voluntary Super Contribution ($)")
y<- as.matrix(ceiling(Inc))
text(x,y+4,labels=as.character(y))

# Financial Literacy
Low <- ifelse(data$C_FinancialLiteracy =="L",1,0)
Low_sum <- sum(Low)
Medium <- ifelse(data$C_FinancialLiteracy =="M",1,0)
Medium_sum <- sum(Medium)
High <- ifelse(data$C_FinancialLiteracy =="H",1,0)
High_sum <- sum(High)
#Creating Vector of Gender
financialLiteracy <- c(Low_sum,Medium_sum, High_sum)
#Creating pie charts and labelling
pie_labels <- paste0(round(100*financialLiteracy/sum(financialLiteracy),2),"%",col=c("L","M","H"))
pie(financialLiteracy, labels =pie_labels,col=c("lightskyblue1","cornsilk","darksalmon" ) ,cex=0.8)
legend("topright", c("L","M","H"), cex = 0.7, col=c("lightskyblue1","cornsilk","darksalmon"),pch =c(15,15,15))
title("Financial Literacy")

data_new <- data
data_new$C_FinancialLiteracy <- factor(data_new$C_FinancialLiteracy, c("L","M","H"))
options(repr.plot.width = 5, repr.plot.height = 1.5)
par(mar = c(9, 8, 8, 8), cex = 0.65)
boxplot(data_new$C_TotalVoluntarySuperContribution~ data_new$C_FinancialLiteracy, 
        xlab="Financial Literacy", ylab="Total Voluntary Super Contribution ($)",
        main ="Financial Literacy - Total Voluntary Super Contribution relationship", ylim=c(0,7500),
        col="lightblue")
fin_lit_L<-subset(data,C_FinancialLiteracy=="L")$C_TotalVoluntarySuperContribution
summary(fin_lit_L)
fin_lit_M<-subset(data,C_FinancialLiteracy=="M")$C_TotalVoluntarySuperContribution
summary(fin_lit_M)
fin_lit_H<-subset(data,C_FinancialLiteracy=="H")$C_TotalVoluntarySuperContribution
summary(fin_lit_H)
# personal financial advice
data_adv <- data
data_adv$C_PersonalFinancialAdvice <- factor(data_adv$C_PersonalFinancialAdvice, c("Y","N"))
options(repr.plot.width = 5, repr.plot.height = 1.5)
par(mar = c(9, 8, 8, 8), cex = 0.65)
boxplot(data_adv$C_TotalVoluntarySuperContribution~ data_adv$C_PersonalFinancialAdvice, 
        xlab="Personal Financial Advice", ylab="Total Voluntary Super Contribution ($)",
        main ="Personal Financial Advice - Total Voluntary Super Contribution relationship", ylim=c(0,7500),
        col="lightblue")
personal_Y<-subset(data,C_PersonalFinancialAdvice=="Y")$C_TotalVoluntarySuperContribution
summary(personal_Y)
personal_N<-subset(data,C_PersonalFinancialAdvice=="N")$C_TotalVoluntarySuperContribution
summary(personal_N)
# show tutorial
data_tut <- data
data_tut$App_ShowTutorial <- factor(data_tut$App_ShowTutorial, c("Y","N"))
options(repr.plot.width = 5, repr.plot.height = 1.5)
par(mar = c(9, 8, 8, 8), cex = 0.65)
boxplot(data_adv$C_TotalVoluntarySuperContribution~ data_tut$App_ShowTutorial, 
        xlab="Show Tutorial", ylab="Total Voluntary Super Contribution ($)",
        main ="Show Tutorial - Total Voluntary Super Contribution relationship", ylim=c(0,7500),
        col="lightblue")