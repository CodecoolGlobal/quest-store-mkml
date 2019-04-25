function sendInfoMentorCard() {
    let mentorName = document.getElementById("mentor-default-name").innerHTML;
    let mentorEmail = document.getElementById("mentor-default-email").innerHTML;
    let mentorClass = document.getElementById("mentor-default-class").innerHTML;

    //Here add function that send these data to DB
}

function editStudentInfoFromMainPage(e){
    let itemList = sendInfoStudentCard(e);
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8000/mentor-students-addupdate");
    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
    request.send(JSON.stringify(itemList));

    location.reload();
}

function editStudentInfoFromAddUpdateSite(e) {
    let itemList = sendInfoStudentCard(e);
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8000/mentor-student-update");
    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
    request.send(JSON.stringify(itemList));

    location.reload();
}

function sendInfoStudentCard(e) {
    let content = document.getElementById(e);

    let studentId = e;
    let studentName = content.getElementsByClassName("student-name")[0].innerHTML;
    let studentEmail = content.getElementsByClassName("student-email")[0].innerHTML;
    let studentClass = content.getElementsByClassName("classes-dropdown")[0].value;
    console.log(studentClass);
    let studentIdInDBuserType = 1;
    let itemList = [studentId, studentName, studentEmail,studentClass,studentIdInDBuserType];

    return itemList;
}



function sendNewStudentData() {
    let userName = document.getElementById("userName").value;
    let userEmail = document.getElementById("userEmail").value;
    let userClass = document.getElementsByClassName("classes-dropdown-add")[0].value;
    console.log(userClass);

    let itemsList = [userName, userEmail, userClass];
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8000/mentor-students-add");
    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
    request.send(JSON.stringify(itemsList));

    location.reload();

}