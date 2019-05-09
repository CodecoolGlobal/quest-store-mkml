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

function addNewItemCard() {
    let itemCardDescrip = document.getElementById("newDescription").innerHTML;
    let itemCardPriceBox = document.getElementById("newPrice").innerHTML;
    let itemCardText = document.getElementById("newCardText").innerHTML;

    let itemsList = [itemCardDescrip, itemCardPriceBox, itemCardText];
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8000/mentor-add-items");
    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
    request.send(JSON.stringify(itemsList));

    location.reload();
}

function sendInfoItemsCard(e) {
    let content = document.getElementById(e);
    let itemCardId = e;
    let itemCardDescrip = content.getElementsByClassName("getName")[0].innerHTML;
    let itemCardPriceBox = content.getElementsByClassName("getPrice")[0].innerHTML;
    let itemCardText = content.getElementsByClassName("getDescription")[0].innerHTML;
    let itemList = [itemCardId, itemCardDescrip, itemCardPriceBox, itemCardText];
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8000/mentor-update-items");
    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
    request.send(JSON.stringify(itemList));

    location.reload();
}

function sendInfoQuestCard() {
    let questCardDescrip = document.getElementById("description").innerHTML;
    let questCardPriceBox = document.getElementById("price").innerHTML;
    let questCardText = document.getElementById("cardText").innerHTML;

    //Here add function that send these data to DB
}


function loadFile(event){

        let output = document.getElementById('emptyImage');
        output.src = URL.createObjectURL(event.target.files[0]);
};

function changeCardColor(){
    document.getElementById("p2").style.color = "blue";
};

/* When the user clicks on the button,
toggle between hiding and showing the dropdown content */
function dropdownMenu() {
    document.getElementById("myDropdown").classList.toggle("show");

// Close the dropdown menu if the user clicks outside of it
    window.onclick = function (event) {
        if (!event.target.matches('.dropbtn')) {
            var dropdowns = document.getElementsByClassName("dropdown-content");
            var i;
            for (i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                }
            }
        }
    }
}
function slider(){
    var range = document.getElementById('range');

    var valueContainers = document.getElementsByClassName("slider-value");
    let handlesStart = [];
    for (let con of valueContainers) {
        handlesStart.push(con.dataset.sliderval);
    }

    noUiSlider.create(range, {

        range: {
            'min': 0,
            'max': 5000
        },

        step: 20,

        // Handles start at ...
        start: handlesStart,

        // ... must be at least 300 apart
        margin: 200,

        // ... but no more than 600
        limit: 3000,

        // Display colored bars between handles
        // connect: [false, true, true, true, true],

        // Put '0' at the bottom of the slider
        orientation: 'horizontal',

        // Move handle on tap, bars are draggable
        behaviour: 'tap-drag',
        tooltips: true,
        format: wNumb({
            decimals: 0
        }),

        // Show a scale with the slider
        pips: {
            mode: 'values',
            values: [100, 200, 1000, 2000, 3000, 4080],
            stepped: true,
            density: 3
        }
    });

    // Give the slider dimensions
    range.style.height = '400px';
    range.style.margin = '0 auto 30px';

    range.noUiSlider.on('update', function (values, handle) {
        valueContainers[handle].innerHTML = values[handle];
    });

    var connect = range.querySelectorAll('.noUi-connect');
    var classes = ['c-1-color', 'c-2-color', 'c-3-color', 'c-4-color', 'c-5-color'];

    for (var i = 0; i < connect.length; i++) {
        connect[i].classList.add(classes[i]);
    }

}

function sendArtifactToBuy(artifactId) {
    let buyData = {"id":artifactId};
    let request = new XMLHttpRequest();
    request.onload = handleBuyResponse;
    request.open("POST", getLocation() + "buy-artifact", true);
    request.setRequestHeader("Content-Type", "application/json");
    request.send(JSON.stringify(buyData));
}

function sendQuestToBuy(questId) {
    let buyData = {"id":questId};
    let request = new XMLHttpRequest();
    request.onload = handleBuyResponse;
    request.open("POST", getLocation() + "claim-quest", true);
    request.setRequestHeader("Content-Type", "application/json");
    request.send(JSON.stringify(buyData));
}

function handleBuyResponse() {
    let status = this.status;
    if (status === 200) {
        window.location.reload();
        // window.location = this.responseURL;
    } else if (status === 500) {
        alert("Transaction failed");
    }
}

function getLocation() {
    let loc = window.location;
    return loc.protocol + "\/\/" + loc.host + "\/";
}