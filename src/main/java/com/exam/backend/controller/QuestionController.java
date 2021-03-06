package com.exam.backend.controller;

import com.exam.backend.model.exam.Question;
import com.exam.backend.model.exam.Quiz;
import com.exam.backend.service.QuestionService;
import com.exam.backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/question")
@CrossOrigin("*")

public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuizService quizService;

    //add question
    @PostMapping("/")
    public ResponseEntity<Question> add(@RequestBody Question question){
        System.out.println(question.toString());
        return ResponseEntity.ok(this.questionService.addQuestion(question));
    }

    //update question
    @PutMapping("/")
    public ResponseEntity<Question> update(@RequestBody Question question){
        return ResponseEntity.ok(this.questionService.updateQuestion(question));
    }

    // get all question of any qid
    @GetMapping("/quiz/{qid}")
    public ResponseEntity<?> getQuestionsOfQuiz(@PathVariable("qid") Long qid){
        Quiz quiz = new Quiz();
        quiz.setqId(qid);
        Set<Question> questionsOfQuiz = this.questionService.getQuestionsOfQuiz(quiz);
        return ResponseEntity.ok(questionsOfQuiz);

        //Optional<Quiz> quiz = this.quizService.getQuiz(qid);
        //Set<Question> questions = quiz.get().getQuestions();
        //List list = new ArrayList(questions);
        //if(list.size() >Integer.parseInt(quiz.get().getNumberOfQuestions())){

          //  list = list.subList(0,Integer.parseInt(quiz.get().getNumberOfQuestions()+1));
        //}
        //Collections.shuffle(list);
        //return ResponseEntity.ok(list);
    }

    //get single question
    @GetMapping("/{questId}")
    public Optional<Question> get(@PathVariable("questId") Long questId){
        return this.questionService.getQuestion(questId);
    }

    //delete question
    @DeleteMapping("/{questId}")
    public void delete(@PathVariable("questId") Long questId){
        this.questionService.deleteQuestion(questId);
    }

    //eval quiz
    @PostMapping("/eval-quiz")
    public ResponseEntity<?> evalQuiz(@RequestBody List<Question> questions){
        double marksGot =0;
        int correctAnswers=0;
        int attempted=0;
        System.out.print(questions);
        for(Question q:questions)
        {
            //single question
            Question question=this.questionService.get(q.getQuesId());
            if(question.getAnswer().equals(q.getGivenAnswer())) {
                //correct
                correctAnswers++;
                double marksSingle =Double.parseDouble(questions.get(0).getQuiz().getMaxMarks())/questions.size();

                marksGot += marksSingle;


            }
            if(!q.getGivenAnswer().trim().equals("")||q.getGivenAnswer()!=null) {
                attempted++;

            }
            System.out.print(q.getAnswer());
        };
        Map<String,Object> map=Map.of("marksGot",marksGot,"correctAnswers",correctAnswers,"attempted",attempted);
        return ResponseEntity.ok(map);

    }



}
