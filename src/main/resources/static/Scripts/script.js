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


function addNewArtifactCard() {
    let itemCardDescrip = document.getElementById("newDescription").innerHTML;
    let itemCardPriceBox = document.getElementById("newPrice").innerHTML;
    let itemCardText = document.getElementById("newCardText").innerHTML;
    console.log(itemCardText);
    //TODO: create list or map to read all data from this Card
    let itemsList = [itemCardDescrip, itemCardPriceBox, itemCardText];
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8000/mentor-items");
    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
    request.send(JSON.stringify(itemsList));

    location.reload();
};
//add image to send to serwer
//Here add function that send these data to DB

function sendInfoQuestCard() {
    let questCardDescrip = document.getElementById("description").innerHTML;
    let questCardPriceBox = document.getElementById("price").innerHTML;
    let questCardText = document.getElementById("cardText").innerHTML;

    //Here add function that send these data to DB
}


function sendInfoItemsCard() {
    let itemCardDescrip = document.getElementById("description").innerHTML;
    let itemCardPriceBox = document.getElementById("price").innerHTML;
    let itemCardText = document.getElementById("cardText").innerHTML;

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

    noUiSlider.create(range, {

        range: {
            'min': 0,
            'max': 5000
        },

        step: 20,

        // Handles start at ...
        start: [1000, 2000, 3000, 4000],

        // ... must be at least 300 apart
        margin: 200,

        // ... but no more than 600
        limit: 3000,

        // Display colored bars between handles
        connect: [false, true, true, true, true],

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

    var valuesDivs = [
        document.getElementById('range-value-1'),
        document.getElementById('range-value-2'),
        document.getElementById('range-value-3'),
        document.getElementById('range-value-4')
    ];

    var diffDivs = [
        document.getElementById('range-diff-1'),
        document.getElementById('range-diff-2'),
        document.getElementById('range-diff-3')
    ];

// When the slider value changes, update the input and span
    range.noUiSlider.on('update', function (values, handle) {
        valuesDivs[handle].innerHTML = values[handle];
        diffDivs[0].innerHTML = values[1] - values[0];
        diffDivs[1].innerHTML = values[2] - values[1];
        diffDivs[2].innerHTML = values[3] - values[2];
    });

    var connect = range.querySelectorAll('.noUi-connect');
    var classes = ['c-1-color', 'c-2-color', 'c-3-color', 'c-4-color', 'c-5-color'];

    for (var i = 0; i < connect.length; i++) {
        connect[i].classList.add(classes[i]);
    }


}

