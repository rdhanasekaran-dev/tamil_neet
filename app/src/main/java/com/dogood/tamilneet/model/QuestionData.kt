package com.dogood.tamilneet.model

class QuestionData(val id:String,val question:String,val uri:String,val option1:String,val option2:String,val option3:String,val option4:String,val option5:String,val option6:String,val difficulty:String,val explanation:String,val explanationImage:String,val correctPercent:String,val origin:String,val subject:String,val answerNr:String){

    constructor():this("","","","","","","","","","","","","","","",""){

    }
}