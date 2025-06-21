$(document).ready(function () {
    const $list = $('#todo-list');
  
    function loadItems() {
      const items = JSON.parse(localStorage.getItem('todo-items')) || [];
      $list.empty();
      items.forEach(text => addItem(text));
    }
  
    function saveItems() {
      const items = [];
      $list.children('li').each(function () {
        items.push($(this).find('.item-text').text());
      });
      localStorage.setItem('todo-items', JSON.stringify(items));
    }
  
    function addItem(text) {
      const $li = $('<li draggable="true"></li>');
      $li.append(`<span class="item-text">${text}</span>`);
      $li.append('<button class="delete-btn">Delete</button>');
      $list.append($li);
    }
  
    $('#add-btn').on('click', function () {
      const text = $('#new-item').val().trim();
      if (text) {
        addItem(text);
        saveItems();
        $('#new-item').val('');
      }
    });
  
    $list.on('click', '.delete-btn', function () {
      $(this).parent().remove();
      saveItems();
    });
  
    let dragged;
  
    $list.on('dragstart', 'li', function () {
      dragged = this;
      $(this).addClass('dragging');
    });
  
    $list.on('dragend', 'li', function () {
      $(this).removeClass('dragging');
    });
  
    $list.on('dragover', 'li', function (e) {
      e.preventDefault();
      const $draggedOver = $(this);
      if (dragged !== this) {
        if ($(dragged).index() < $draggedOver.index()) {
          $draggedOver.after(dragged);
        } else {
          $draggedOver.before(dragged);
        }
      }
    });
  
    $list.on('drop', function () {
      saveItems();
    });
  
    loadItems();
  });
  