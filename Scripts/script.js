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
    var questCardDescrip = document.getElementById("description").innerHTML;
    var questCardPriceBox = document.getElementById("price").innerHTML;
    var questCardText = document.getElementById("cardText").innerHTML;

    //Here add function that send these data to DB
}