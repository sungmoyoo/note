-- 커뮤니티 홈 화면 조회
select
  b.board_no,
  b.title,
  b.created_date,
  count(bl.member_no) like_count,
  m.name,
  h.head_content,
  bf.file_no,
  bf.uuid_file_name
from
  board b
  inner join head h on b.head_no=h.head_no
  inner join member m on b.member_no=m.member_no
  left join board_like bl on b.board_no=bl.board_no
  left join board_file bf on b.board_no=bf.board_no
group by
  b.board_no
order by
  like_count desc;

  -- 정보공유/자유 게시판 리스트 조회
-- 정렬은 기본 최신순, 추천순, 조회순은 order by만 바꾸면 됨
select 
  b.board_no,
  b.title,
  b.view_count,
  b.created_date,
  h.head_content,
  m.name,
  count(distinct bl.member_no) like_count,
  count(distinct c.comment_no) + count(distinct r.reply_no) as comment_reply_count
from 
  board b 
  inner join head h on b.head_no=h.head_no
  inner join member m on b.member_no=m.member_no
  inner join board_like bl on b.board_no=bl.board_no
  left join comment c on b.board_no = c.board_no
  left join reply r on c.comment_no = r.comment_no
where
  b.category_no=#{category}
group by
  b.board_no
order by
  b.created_date;