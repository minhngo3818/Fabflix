import { getParameterByName, getRandomFloat } from "./utils.js";
import {
    renderPaginationInfo,
    toPrevPage,
    toNextPage,
    disableNextPrevButtons,
    getCurrentPage,
    getCurrentPageSize
} from "./pagination.js"
import { SortableTable } from "./sort-table.js";
import { addCartItem } from "./cart-crud.js";

function renderData(dataJson) {
    const tableBody = jQuery("#movie-list-tbody")
    const movieData = dataJson["data"]
    let currentPage =  getCurrentPage()
    let pageSize = getCurrentPageSize()

    for (let i = 0; i < movieData.length; i++) {
        // Create genre list string
        let genresHTML = movieData[i]["genres"].map(genre =>
            `<a href="./movie-list.jsp?browse=true&genreId=${genre["id"]}">${genre["name"]}</a>`).join(", ")

        // Create star list string
        let starsHTML = movieData[i]["stars"].map(star =>
            `<a href="./single-star.jsp?id=${star["id"]}">${star["name"]}</a>`
        ).join(", ");

        const movieHTML = `<tr data-index="${movieData[i].id}">
                <td>${i + 1 + (currentPage - 1) * pageSize}</td>
                <td><a href="./single-movie.jsp?id=${movieData[i].id}">${movieData[i].title}</a></td> 
                <td>${movieData[i]["year"]}</td>
                <td>${movieData[i]["director"]}</td>
                <td>${movieData[i]["rating"]}</td>
                <td>${genresHTML}</td>
                <td>${starsHTML}</td>
                <td>${getRandomFloat(1.99, 59.99)}</td>
                <td><button name="add-to-cart-btn" class="primary-btn">Add to cart</button></td>
            </tr>`

        tableBody.append(movieHTML)
    }

    document.querySelectorAll('button[name="add-to-cart-btn"]').forEach(button => {
        button.addEventListener("click", addItemHandler)
    });

    renderPaginationInfo(dataJson["count"], page)
    disableNextPrevButtons(dataJson["count"])
}

function addItemHandler() {
    const row = this.closest('tr');

    // Get data from a row
    const cells = row.querySelectorAll('td');
    const data = {
        movieId: row.getAttribute("data-index"),
        movieTitle: cells[1].querySelector('a').textContent,
        unitPrice: cells[7].textContent,
        quantity: 1
    };

    addCartItem(data)
}

window.addEventListener('load', function () {
    let sortableTable = document.getElementById('movie-list-table');
    new SortableTable(sortableTable);
});

// Set click handlers for next/prev buttons
document.getElementById("next-page").addEventListener("click", toNextPage )
document.getElementById("prev-page").addEventListener("click", toPrevPage )

// Select page size
let selectComponent = document.getElementById("page-count-selection")
selectComponent.onchange = (event) => {
    let currentPage = getCurrentPage()
    let pageSize = event.target.value;
    let totalRecords = parseInt(document.getElementById("total-records").innerText)

    let url = new URL(window.location.href);
    let params = new URLSearchParams(url.search);
    params.set("pageSize", pageSize);

    if (pageSize * currentPage > totalRecords) params.set("page", Math.ceil(totalRecords / pageSize))

    window.location.href = url.origin + url.pathname + '?' + params.toString();
}

window.addEventListener('load', function () {
    let currentPageSize = getCurrentPageSize()

    for (let i = 0; i < selectComponent.options.length; i++) {
        if (selectComponent.options[i].text === currentPageSize) {
            selectComponent.selectedIndex = i;
            break;
        }
    }
})

let page = getParameterByName("page")
let pageSize = getParameterByName("pageSize")

// Search params
let isSearch = getParameterByName("search")
let title = getParameterByName("title")
let year = getParameterByName("year")
let director = getParameterByName("director")
let starName = getParameterByName("starName")
let keywords = getParameterByName("keywords")

// Browse params
let isBrowse = getParameterByName("browse")
let genreId = getParameterByName("genreId")
let startWith = getParameterByName("startWith")

// Initial load
if (isSearch === "true") {
    let api = ""
    if (keywords && keywords !== "null")
        api += `api/movie-list/search?keywords=${keywords}`
    else
        api = `api/movie-list/search?title=${title}&year=${year}&director=${director}&starName=${starName}`

    api += `&page=${page}&pageSize=${pageSize}`

    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: api,
        success: (resultData) => renderData(resultData)
    })
}

if (isBrowse === "true") {
    let api = `api/movie-list/browse?genreId=${genreId}&startWith=${startWith}`
    api += `&page=${page}&pageSize=${pageSize}`

    jQuery.ajax({
        dataType: "json",
        method: "GET",
        url: api,
        success: (resultData) => renderData(resultData)
    })
}

