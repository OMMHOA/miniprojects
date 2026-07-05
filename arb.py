class Bookie:
  def __init__(self, multipliers):
    self.multipliers = multipliers


class ArbEvaluationResponse:
  def __init__(self, profitable, roi=None, rates=None, bets=None):
    self.profitable = profitable
    self.roi = roi
    self.rates = rates
    self.bets = bets


class ArbEvaluationRequest:
  def __init__(self, *bookies):
    self.bookies = bookies


MULTIPLIER = 10000


def is_profitable(*odds):
  return sum(map(lambda x: MULTIPLIER/x, odds)) < MULTIPLIER


def roi(px, py):
  return (px*py - px - py) * 100 / (px + py)


def rates(*odds):
  px = odds[0]
  return [px / p for p in odds]


def evaluate(request: ArbEvaluationRequest) -> ArbEvaluationResponse:
  """
    Supports only 2 outcomes for 2 bookies for now.
  """
  bookie1 = request.bookies[0]
  bookie2 = request.bookies[1]
  options = [(bookie1, bookie2), (bookie2, bookie1)]
  best = None
  for b1, b2 in options:
    o1 = b1.multipliers[0]
    o2 = b2.multipliers[1]
    if is_profitable(o1, o2):
      r = roi(o1, o2)
      if best is None or best.roi < r:
        best = ArbEvaluationResponse(
          profitable = True,
          roi = r,
          rates = rates(o1, o2),
          bets = [b1, b2]
        )
  if best is None:
    return ArbEvaluationResponse(profitable = False)
  else:
    return best


b1 = Bookie([1.4, 1.5])
b2 = Bookie([1.3, 4])
r = evaluate(ArbEvaluationRequest(b1, b2))
print(r.profitable)
print(r.roi)
print(r.rates)
print(f"{r.bets[0].multipliers[0]} {r.bets[1].multipliers[1]}")
