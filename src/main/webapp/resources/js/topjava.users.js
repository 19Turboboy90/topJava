const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

function enable(checkbox) {
    let enabled = $(checkbox).closest('tr');
    $.ajax({
        type: "PATCH",
        url: ctx.ajaxUrl + enabled.attr("id") + '/enabled?enabled=' + checkbox.checked
    })
        .done(function () {
            successNoty("User " + (checkbox.checked ? 'enabled' : 'disabled'));
        })
        .fail(function () {
            checkbox.checked = !checkbox.checked;
        })
        .always(function () {
            enabled.attr("data-user-enabled", checkbox.checked);
        });
}

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});