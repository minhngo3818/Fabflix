import {getParameterByName, getRandomFloat} from "./utils.js";
import { addCartItem } from "./cart-crud.js";

function renderData(data) {
    let movieInfo = jQuery("#movie-info")
    let movieData = data[0]

    movieInfo.attr("data-index", movieData["id"]) 

    let genresHTML = movieData["genres"]
        .sort((a, b) => a["name"].localeCompare(b["name"]))
        .map(genre =>
        `<a href="./movie-list.jsp?browse=true&genreId=${genre["id"]}">${genre["name"]}</a>`).join(", ")
    let movieInfoHTML = `
        <div><h3 id="movie-info-title">${movieData["title"]}</h3></div>
        <div>Year: <span>${movieData["year"]}</span></div>
        <div>Director: <span id="movie-info-director">${movieData["director"]}</span></div>
        <div>Genres: <span>${genresHTML}</span></div>
        <div>Price: $<span id="movie-info-price">${getRandomFloat(1.99, 4.99)}</span></div>
        <button class="primary-btn" type="button" id="add-to-card">Add to cart</button>

    `
    movieInfo.append(movieInfoHTML)

    let movieStarsTbody = jQuery("#movie-stars-tbody")
    let starRowHTML = movieData["stars"].map(star =>
        `
            <tr>
                <td><a href="./single-star.jsp?id=${star["id"]}">${star["name"]}</a></td>
                <td>${star["birthYear"] ?? "N/A"}</td>
            </tr>
        `
        ).join("")
    movieStarsTbody.append(starRowHTML)

    document.getElementById("add-to-card").addEventListener("click", addMovieToCart)
}


function addMovieToCart() {
    let id = jQuery("#movie-info").attr("data-index")
    let title = jQuery("#movie-info-title").text()
    let quantity = 1
    let unitPrice = jQuery("#movie-info-price").text()

    const data = {
        movieId : id,
        movieTitle: title,
        quantity: quantity,
        unitPrice: unitPrice
    }

    addCartItem(data)
}


let movieId = getParameterByName("id")

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: `api/single-movie?id=${movieId}`,
    success: (result) => renderData(result)
})