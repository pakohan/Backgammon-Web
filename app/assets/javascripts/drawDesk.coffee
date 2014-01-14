root = exports ? this

root.paintStones = (canvas, image, x, y, a, msg)->
    i = 0
    while i < msg.numberStones
        i++
        if a < 12 or a is 24 or a is 26
            canvas.drawIII image, x, y + i * 25
        else
            canvas.drawIII image, x, y - i * 25

root.getCoordinates = (canvas, image, a, msg) ->
    xCoordinate = 645

    if a >= 6 and a < 18
        xCoordinate -= 2 * 30
    else
        switch a
            when 24 then root.paintStones canvas, image, 340, 35, a, msg
            when 25 then root.paintStones canvas, image, 340, 575, a, msg
            when 26 then root.paintStones canvas, image, 690, 35, a, msg
            when 27 then root.paintStones canvas, image, 690, 575, a, msg
    yCoordinate
    if a < 12
        yCoordinate = 35
        xCoordinate -= a % 12 * 50
        root.paintStones canvas, image, xCoordinate, yCoordinate, a, msg
    else if a > 11 and a < 24
        yCoordinate = 565
        xCoordinate -= (23 - a) % 12 * 50;
        root.paintStones canvas, image, xCoordinate, yCoordinate, a, msg

root.drawStones = (canvas, msg)->
    for field, i in msg
        image = null

        if field.stoneColor == 0 then image = document.getElementById 'stonew'
        else image =  document.getElementById 'stoneb'

        root.getCoordinates canvas, image, i, field

root.drawDices = (canvas, i, dice)->
    diceX = 440 + i * 60

    if dice > 0
      canvas.drawIII document.getElementById('dice' + dice), diceX, 295

root.drawBackground = (canvas)->
    img = document.getElementById 'background'

    canvas.drawIII img, 0, 0

root.paintComponent = (msg)->
    console.log msg

    state = "#{msg.status}<br>#{msg._id}"
    $("#status").html state

    c = document.getElementById 'game'
    canvas = c.getContext '2d'

    canvas.drawIII = (image, x, y)->
        canvas.drawImage image, x, y

    root.drawBackground canvas
    root.drawStones canvas, msg.fields

    root.drawDices canvas, 0, msg.dice.valuesToDraw[0]
    root.drawDices canvas, 1, msg.dice.valuesToDraw[1]
    root.drawDices canvas, 2, msg.dice.valuesToDraw[2]
    root.drawDices canvas, 3, msg.dice.valuesToDraw[3]

getMousePos = (canvas, evt) ->
  rect = canvas.getBoundingClientRect()
  x: evt.clientX - rect.left
  y: evt.clientY - rect.top

canvas = document.getElementById("game")
context = canvas.getContext("2d")

canvas.addEventListener "click", ((evt) ->
  mousePos = getMousePos(canvas, evt)
  mousePos["id"] = msg._id
  $.post "/json", mousePos, (data) ->
    root.paintComponent data

), false
