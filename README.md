# binance-stream
<a href="https://developers.binance.com/docs/derivatives/usds-margined-futures/websocket-market-streams">Дока на api</a>

<b>Предметная область:</b> анализ криптовалюты(веб-сокет wss://fstream.binance.com/ws/) 
<p>
  <b>Какие данные для этой области являются потоковыми?</b>
  Непрерывные потоки контрактов Kline/Candlestick
  скорость обновления 250ms
  пример ответа : <br>
  { <br>
  &nbsp&nbsp&nbsp"e":"continuous_kline",	// Event type <br>
  &nbsp&nbsp&nbsp"E":1607443058651,		// Event time <br>
  &nbsp&nbsp&nbsp"ps":"BTCUSDT",			// Pair <br>
  &nbsp&nbsp&nbsp"ct":"PERPETUAL"			// Contract type <br>
  &nbsp&nbsp&nbsp"k":{ <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"t":1607443020000,		// Kline start time <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"T":1607443079999,		// Kline close time <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"i":"1m",				// Interval <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"f":116467658886,		// First updateId <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"L":116468012423,		// Last updateId <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"o":"18787.00",			// Open price <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"c":"18804.04",			// Close price <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"h":"18804.04",			// High price <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"l":"18786.54",			// Low price <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"v":"197.664",			// volume <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"n": 543,				// Number of trades  <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"x":false,				// Is this kline closed? <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"q":"3715253.19494",	// Quote asset volume <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"V":"184.769",			// Taker buy volume <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"Q":"3472925.84746",	//Taker buy quote asset volume <br>
    &nbsp&nbsp&nbsp&nbsp&nbsp"B":"0"					// Ignore <br>
  &nbsp&nbsp&nbsp} <br>
} <br>
</p>
<p>
  <b>Какие результаты мы хотим получить в результате обработки?</b> 
  Статистика за определенное время суток, часа... Возможно можно попробовать вычислить какие-то паттерны технического анализа
</p>
<p>
  <b>Как в процессе обработки можно задействовать машинное обучение?</b> 
  Для предсказания цены на следующий временной промежуток.
</p>
<p>
  <b>Как предметная область относится к запаздыванию обработки? Насколько это критично?</b> 
  Для предметной области задержка критична даже если она <1мс. Но в нашем случае api не сильно быстрое и поэтому 250ms задержка является более чем приемлемой для минутного интервала.
</p>
<p>
  <b>Как предметная область относится к потере данных? Насколько это критично? Какую семантику (не менее одного раза, не более одного раза, ровно один раз) следует выбрать?</b>
  Для предметной области потеря крайне критична. 
  Для работы потеря критична.
  Имеет смысл выбрать семантику at least once.
</p>
