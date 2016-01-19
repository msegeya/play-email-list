$ ->
  $.get "/address", (data) -> # call the /address route and get a JSON list of data. 
    $.each data, (index, address) -> # loop through each element in the list.
      $("#addresses").append $("<li>").text address.address # append to the addresses ID the address as a <li>. 