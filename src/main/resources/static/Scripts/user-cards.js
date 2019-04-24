function sendInfoMentorCard() {
    let mentorName = document.getElementById("mentor-default-name").innerHTML;
    let mentorEmail = document.getElementById("mentor-default-email").innerHTML;
    let mentorClass = document.getElementById("mentor-default-class").innerHTML;

    //Here add function that send these data to DB
}

function sendInfoStudentCard(e) {
    let content = document.getElementById(e);

    let studentId = e;
    let studentName = content.getElementsByClassName("student-name")[0].innerHTML;
    let studentEmail = content.getElementsByClassName("student-email")[0].innerHTML;
    let studentClass = content.getElementsByClassName("student-class-id")[0].innerHTML;
    let itemList = [studentId, studentName, studentEmail,studentClass];

    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8000/mentor");
    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
    request.send(JSON.stringify(itemList));

    location.reload();
}