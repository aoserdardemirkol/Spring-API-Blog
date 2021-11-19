SELECT
	e.entry_id,
	title AS "Entry title",
	e.content AS "Entry content",
	e.create_date "Entry create date",
	e.update_date "Entry update date",
	c.content AS "Comment content",
	c.create_date AS "Comment create date",
	c.update_date  AS "Comment update date"
FROM
	entry AS e
INNER JOIN comment AS c ON e.entry_id = c.entry_id;