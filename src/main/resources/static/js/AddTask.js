const chBoxes =
    document.querySelectorAll('.dropdown-menu input[type="checkbox"]');
const dpBtn =
    document.getElementById('multiSelectDropdown');
let mySelectedListItems = [];

function handleCB() {
    mySelectedListItems = [];
    let mySelectedListItemsText = '';

    chBoxes.forEach((checkbox) => {
        if (checkbox.checked) {
            mySelectedListItems.push(checkbox.value);
            mySelectedListItemsText += checkbox.value + ', ';
        }
    });

    dpBtn.innerText =
        mySelectedListItems.length > 0
            ? mySelectedListItemsText.slice(0, -2) : 'Select';
}

chBoxes.forEach((checkbox) => {
    checkbox.addEventListener('change', handleCB);
});