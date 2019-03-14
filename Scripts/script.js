function progressBar() {
    let width = 8;
    let elem = document.getElementById("myBar");
    let userCollectCC = 99;
    let levelStart = 0; //Level start when user have ...CC
    let levelEnd = 100; //Level end when user have ...CC

    let barValue = levelEnd - levelStart;
    let userProgress = (userCollectCC*100)/barValue;

    setInterval(frame, 30);
    function frame() {
        if (width <= userProgress)
            width++;
            elem.style.width = width + '%';
        }
}


function sendInfoQuestCard() {
    let questCardDescrip = document.getElementById("description").innerHTML;
    let questCardPriceBox = document.getElementById("price").innerHTML;
    let questCardText = document.getElementById("cardText").innerHTML;

    //Here add function that send these data to DB
}

function changeCardColor(){
    document.getElementById("p2").style.color = "blue";
}