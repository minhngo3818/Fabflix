function renderMetaTableColumns(columnList) {
    return columnList.map(column => {
        return `<tr>
            <td>${column["name"]}</td>
            <td>${column["type"]}</td>
        </tr>`
    }).join("")
}

function renderMetaTable(tableInfo) {
    return `
        <div class="metadb-block">
            <h2>${tableInfo["name"]}</h2>
            <table class="table-list">
                <thead>
                    <tr>
                        <th>Column Name</th>
                        <th>Type</th>
                    </tr>
                </thead>
                <tbody>${renderMetaTableColumns(tableInfo["columns"])}</tbody>
            </table>
        </div>
    `
}

function handleResult(jsonData) {
    let metadbListComponent = $("#metadb-list")
    jsonData.forEach(tableInfo => {
            let tableInfoComponent = document.createElement("div")
            tableInfoComponent.className = "metadb-block"
            tableInfoComponent.innerHTML = renderMetaTable(tableInfo)
            metadbListComponent.append(tableInfoComponent)
        }
    )
}

function handleError(errorResponse) {
    Toastify({
        text: errorResponse["message"],
        gravity: "top",
        position: "center",
        duration: 2000,
        close: true,
        backgroundColor: "#ca3f49",
    }).showToast();
}


jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/metadata-db",
    success: (result) => handleResult(result),
    error: (jqXHR) => handleError(jqXHR.request.text())
})