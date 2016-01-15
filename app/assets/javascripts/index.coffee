$ ->
  $.get "/address", (data) ->
    $.each data, (index, address) ->
      $("#addresses").append $("<li>").text address.address