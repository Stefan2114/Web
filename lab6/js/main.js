$('#load').on('click', function () {
    const type = $('#filter').val();
    localStorage.setItem('lastFilter', type);
    $.getJSON('api.php', { action: 'filter', type }, function (data) {
      $('#recipe-list').empty();
      data.forEach(r => {
        $('#recipe-list').append(`
          <li>
            <strong>${r.name}</strong> by ${r.author} (${r.type})<br>
            <button onclick="edit(${r.id})">Edit</button>
            <button onclick="del(${r.id})">Delete</button>
          </li>
        `);
      });
      $('#lastFilter').text('Last filter: ' + type);
    });
  });
  
  function del(id) {
    if (!confirm('Delete this recipe?')) return;
    $.post('api.php', { action: 'delete', id }, function (res) {
      alert(res.message);
      $('#load').click();
    }, 'json');
  }
  
  function edit(id) {
    location.href = 'edit.html?id=' + id;
  }
  
  // Load last filter automatically
  const last = localStorage.getItem('lastFilter');
  if (last) {
    $('#filter').val(last);
    $('#load').click();
  }
  