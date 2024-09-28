// Search section
let searchForm = $("#search-form")
function handleSubmitSearch(event) {
    event.preventDefault()

    let title = $("#se-title").val()
    let year = $("#se-year").val()
    let director = $("#se-director").val()
    let starName = $("#se-star-name").val()

    let redirectUrl = `./movie-list.jsp?search=true`
    redirectUrl += `&title=${title}`
    redirectUrl += `&year=${year}`
    redirectUrl += `&director=${director}`
    redirectUrl += `&starName=${starName}`

    window.location.href = redirectUrl
}

searchForm.submit(handleSubmitSearch)

//  Browse section
function addGenresToPopup() {
    jQuery.ajax({
        dataType: "json",
        method: "GET",
        url: `api/genre-list`,
        success: (genres) => {
            let genresSelection = document.getElementById("browse-genres")
            genres.forEach(genre => {
                var hyperlink = document.createElement("a");
                hyperlink.text = genre["name"];
                hyperlink.href = `./movie-list.jsp?browse=true&genreId=${genre["id"]}`
                genresSelection.appendChild(hyperlink);
            })
        }
    })
}

function addTitleToPopup() {
    // Render letters from A-Z                
    let alphabetSpan = document.getElementById("alphabet-span")
    for (let i = 65; i < 91; i++) {
        var hyperlink = document.createElement("a");
        let letter = String.fromCharCode(i)
        hyperlink.text = letter;
        hyperlink.href = `./movie-list.jsp?browse=true&startWith=${letter}`
        alphabetSpan.appendChild(hyperlink);
    }
    
    // Render numbers from 0 to 9
    let numsSpan = document.getElementById("nums-span")
    for (let i = 0; i < 10; i++) {
        var hyperlink = document.createElement("a");
        hyperlink.text = i;
        hyperlink.href = `./movie-list.jsp?browse=true&startWith=${i}`
        numsSpan.appendChild(hyperlink);
    }

    // Render symbol represent for other characters
    let otherSpan = document.getElementById("others-span")
    var hyperlink = document.createElement("a");
    hyperlink.text = "<*>";
    hyperlink.href = `./movie-list.jsp?browse=true&startWith=*`
    otherSpan.appendChild(hyperlink);
}


function handleOpenPopup(buttonComp, popupContainerComp) {
    buttonComp.addEventListener("click", () => {
        popupContainerComp.style.visibility = "visible"
    })
}


function handleOutsideClick(container) {
    document.addEventListener("click", (e) => {
        if (e.target === container && container.style.visibility === "visible") {
            e.preventDefault()
            container.style.visibility = "hidden";
            console.log("container was clicked")
        }
    });
}

let genresPopupBtn = document.getElementById("genres-popup-btn")
// let genresPopup = document.getElementById("genres-popup");
let genresPopupContainer = document.getElementById("genres-popup-container");
handleOpenPopup(genresPopupBtn, genresPopupContainer)
handleOutsideClick( genresPopupContainer);

let titlePopupBtn = document.getElementById("title-popup-btn")
// let titlePopup = document.getElementById("title-popup");
let titlePopupContainer = document.getElementById("title-popup-container");
handleOpenPopup(titlePopupBtn, titlePopupContainer)
handleOutsideClick(titlePopupContainer);

document.addEventListener("DOMContentLoaded", () => {
    addGenresToPopup()
    addTitleToPopup()
})





