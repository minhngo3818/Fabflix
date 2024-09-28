let add_genre_form = $("#add-genre-db-form")

function handleSuccess(resultData) {

    console.log(resultData)

    let resultLog = $("#add-result")

    if (resultData["status"] === "success") {
        let resultP = `<p class="result-message">Success: genreId:${resultData["data"]["genreId"]}</p>`;
        resultLog.append(resultP)
    } else {
        let resultP = `<p class="result-message fail">Success: Error: ${resultData["message"]}</p>`
        resultLog.append(resultP)
    }
}

function handleError(resultError) {
    let jsonResult = JSON.parse(resultError)
    Toastify({
        text: jsonResult["message"],
        gravity: "top",
        position: "center",
        duration: 2000,
        close: true,
        backgroundColor: "#ca3f49",
    }).showToast();
}


function handleSubmitAddMovie(formSubmitEvent) {
    formSubmitEvent.preventDefault()
    jQuery.ajax({
        dataType: "json",
        method: "POST",
        url: "api/add-genre",
        data: add_genre_form.serialize(),
        success: (resultData) => handleSuccess(resultData),
        error: (jqXHR) => handleError(jqXHR.responseText)
    })
}

add_genre_form.submit(handleSubmitAddMovie)