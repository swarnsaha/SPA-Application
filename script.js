const startPage = document.getElementById("start-page");
const startQuizForm = document.getElementById("start-quiz-form");
const userNameInput = document.getElementById("user-name");
const quizSelection = document.getElementById("quiz-selection");
const quizContainer = document.getElementById("quiz-container");
const scoreboard = document.getElementById("scoreboard");
const questionsAnswered = document.getElementById("questions-answered");
const elapsedTime = document.getElementById("elapsed-time");
const totalScore = document.getElementById("total-score");
const quizContent = document.getElementById("quiz-content");
const feedbackContainer = document.getElementById("feedback-container");
const feedbackMessage = document.getElementById("feedback-message");
const nextQuestionButton = document.getElementById("next-question");
const resultPage = document.getElementById("result-page");
const resultMessage = document.getElementById("result-message");
const retakeQuizButton = document.getElementById("retake-quiz");
const returnMainButton = document.getElementById("return-main");
let userName;
let quizId;
let quizData;
let currentQuestionIndex = 0;
let correctAnswers = 0;
let timer;
startQuizForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    userName = userNameInput.value.trim();
    quizId = quizSelection.value;
    if (!userName || !quizId) return;
    quizData = await fetchQuizData(quizId);
    if (!quizData) return;
    startPage.classList.add("d-none");
    quizContainer.classList.remove("d-none");
    startTimer();
    displayQuestion();
});
async function fetchQuizData(quizId) {
    try {
        const response = await fetch(`https://my-json-server.typicode.com/swarnsaha/quiz/${quizId}`);
        return await response.json();
    } catch (error) {
        console.error("Error fetching quiz data:", error);
        return null;
    }
}
function startTimer() {
    let seconds = 0;
    timer = setInterval(() => {
        seconds++;
        elapsedTime.textContent = seconds;
    }, 1000);
}
function displayQuestion() {
    const question = quizData.questions[currentQuestionIndex];
    if (!question) return;
    const questionTemplate = Handlebars.compile(document.getElementById(`${question.type}-template`).innerHTML);
    quizContent.innerHTML = questionTemplate(question);
    quizContent.addEventListener("click", evaluateAnswer, { once: true });
}
function evaluateAnswer(event) {
    if (!event.target.matches(".option")) {
        quizContent.addEventListener("click", evaluateAnswer, { once: true });
        return;
    }
    const question = quizData.questions[currentQuestionIndex];
    const userAnswer = event.target.textContent;
    const correctAnswer = question.correctAnswer;
    if (userAnswer === correctAnswer) {
        feedbackMessage.textContent = "Correct!";
        correctAnswers++;
    } else {
        feedbackMessage.textContent = `Incorrect. The correct answer is: ${correctAnswer}`;
    }
    totalScore.textContent = Math.round((correctAnswers / (currentQuestionIndex + 1)) * 100);
    questionsAnswered.textContent = currentQuestionIndex + 1;
    feedbackContainer.classList.remove("d-none");
}
nextQuestionButton.addEventListener("click", () => {
    feedbackContainer.classList.add("d-none");
    currentQuestionIndex++;
    if (currentQuestionIndex < quizData.questions.length) {
        displayQuestion();
    } else {
        endQuiz();
    }
});
function endQuiz() {
    clearInterval(timer);
    const finalScore = parseInt(totalScore.textContent, 10);
    if (finalScore >= 80) {
        resultMessage.textContent = `Congratulations ${userName}! You passed the quiz!`;
    } else {
        resultMessage.textContent = `Sorry ${userName}, you failed the quiz.`;
quizContainer.classList.add("d-none");
resultPage.classList.remove("d-none");
}
retakeQuizButton.addEventListener("click", () => {
resetQuiz();
quizContainer.classList.remove("d-none");
resultPage.classList.add("d-none");
startTimer();
displayQuestion();
});
returnMainButton.addEventListener("click", () => {
resetQuiz();
startPage.classList.remove("d-none");
resultPage.classList.add("d-none");
});
function resetQuiz() {
clearInterval(timer);
currentQuestionIndex = 0;
correctAnswers = 0;
elapsedTime.textContent = "0";
totalScore.textContent = "0";
questionsAnswered.textContent = "0";
}
Handlebars.registerHelper("inc", function (value) {
return parseInt(value, 10) + 1;
});}